package br.com.ifix.ifix;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import DB.DataBase;
import api.HttpGlobalRetrofit;
import api.Response.Token;
import api.deserializers.TokenDes;
import api.interfaces.UserInterface;
import api.requests.ApiClient;
import models.Credential;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ifix);
        db = new DataBase(this);
        login();
    }


    private void login() {
        final Credential credential = db.getCredentials();
        if(credential.getUsername() != null && credential.getAccess_token() != null) {
            GregorianCalendar hoje = new GregorianCalendar();
            hoje.setTime(new Date());
            GregorianCalendar expires = new GregorianCalendar();
            expires.setTime(credential.getExpires_in());
            if(hoje.before(expires)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            } else {
                final ApiClient client =  new ApiClient(
                        getString(R.string.grant_type),
                        Integer.parseInt(getString(R.string.client_id)),
                        getString(R.string.client_secret),
                        credential.getUsername(),
                        credential.getPassword(),
                        getString(R.string.scope));

                Gson gson = new GsonBuilder().registerTypeAdapter(Token.class, new TokenDes()).create();
                HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(gson);
                UserInterface req = globalRetrofit.getRetrofit().create(UserInterface.class);

                Call<Token> generateToken = req.getToken(client);


                generateToken.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        int code = response.code();
                        if(code == 200) {
                            Token token = response.body();
                            if(token != null){
                                if(token.getAccess_token() != null && token.getExpires_in() > 0){
                                    GregorianCalendar expires = new GregorianCalendar();
                                    expires.setTime(new Date());
                                    expires.add(Calendar.DAY_OF_MONTH, token.getExpires_in()/86400);

                                    credential.setAccess_token(token.getAccess_token());
                                    credential.setExpires_in(expires.getTime());
                                    //db.updateCredentials(credential);
                                }
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Falha ao tentar autenticar", Toast.LENGTH_LONG).show();
                                }
                            }, SPLASH_TIME_OUT);

                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Error de conexão", Toast.LENGTH_LONG).show();
                            }
                        }, SPLASH_TIME_OUT);

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
    }
}
}
