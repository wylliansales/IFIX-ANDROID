package br.com.ifix.ifix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import tools.Notification;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    ProgressDialog dialog;
//    SharedPreferences user_credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
            }
        });

        ImageView mEmailSignInButton = (ImageView) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView button_create_user = (TextView) findViewById(R.id.create_user);
        button_create_user.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Tentar logar na conta do usuário.
     * Se houver erros de formulário (e-mail inválido, campos ausentes, etc.), o
     * erros são apresentados e nenhuma tentativa de login real é feita.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Armazena os valores no momento da tentativa de login
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Verifique se há uma senha válida, se o usuário entrou em.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Verifica se o endereço de email é válido.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            this.generateToken();
        }
    }

    private void generateToken(){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Autenticando aplicação...");
        dialog.setCancelable(false);
        dialog.show();

        final ApiClient client =  new ApiClient(
                getString(R.string.grant_type),
                Integer.parseInt(getString(R.string.client_id)),
                getString(R.string.client_secret),
                this.mEmailView.getText().toString(),
                this.mPasswordView.getText().toString(),
                getString(R.string.scope));


        Gson gson = new GsonBuilder().registerTypeAdapter(Token.class, new TokenDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getApplicationContext(),gson);
        UserInterface req = globalRetrofit.getRetrofit().create(UserInterface.class);

        Call<Token> generateToken = req.getToken(client);


        generateToken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(dialog.isShowing())
                    dialog.dismiss();

                int code = response.code();
                if(code == 200) {
                    Token token = response.body();

                    if(token != null){
                        if(token.getAccess_token() != null && token.getExpires_in() > 0){
                            GregorianCalendar hoje = new GregorianCalendar();
                            hoje.setTime(new Date());
                            hoje.add(Calendar.DAY_OF_MONTH, token.getExpires_in()/86400);

                            Credential cred = new Credential();
                            cred.setAccess_token(token.getAccess_token());
                            cred.setExpires_in(hoje.getTime());
                            cred.setUsername(client.getUsername());
                            cred.setPassword(client.getPassword());

                            boolean save = saveCredential(cred);
                            if(save) {
                                Notification.notify(getApplicationContext(),"Bem vindo :)", 0);
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Notification.notify(getApplicationContext(),"Não foi possível gravar credenciais", 0);
                            }
                        }
                    } else {
                        Notification.notify(getApplicationContext(),"Holve uma falha, tente novamente", 0);
                    }
                } else if(code == 401) {
                    Notification.notify(getApplicationContext(),"Dados incorretos", 0);
                } else {
                    Notification.notify(getApplicationContext(),"Falha: " + String.valueOf(code), 0);
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });
        //return false;
    }

    private boolean saveCredential(Credential credential){
        try{
            DataBase db = new DataBase(this);
            db.insertCredentials(credential);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

}

