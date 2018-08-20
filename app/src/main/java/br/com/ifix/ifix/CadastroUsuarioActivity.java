package br.com.ifix.ifix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import api.HttpGlobalRetrofit;
import api.Response.User;
import api.deserializers.UserDes;
import api.interfaces.UserInterface;
import api.requests.UserReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Notification;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPpasswordView;
    private EditText mPasswordConfirmationView;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        mNameView = (EditText) findViewById(R.id.user_name);
        mEmailView = (EditText) findViewById(R.id.user_email);
        mPpasswordView = (EditText) findViewById(R.id.user_password);
        mPasswordConfirmationView = (EditText) findViewById(R.id.user_password_confirmation);

        TextView createUserButton = (TextView) findViewById(R.id.user_create_button);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        //Resert Erros
        mNameView.setError(null);
        mEmailView.setError(null);
        mPpasswordView.setError(null);
        mPasswordConfirmationView.setError(null);

        //Armazena os valores
        String name                 = mNameView.getText().toString();
        String email                = mEmailView.getText().toString();
        String password             = mPpasswordView.getText().toString();
        String password_confirmation= mPasswordConfirmationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Verifique se há uma senha válida, se o usuário entrou em.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPpasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPpasswordView;
            cancel = true;
        } else if(!password.equalsIgnoreCase(password_confirmation)) {
            mPasswordConfirmationView.setError("Senha não confere");
            focusView = mPasswordConfirmationView;
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

        //Verifica se
        if(TextUtils.isEmpty(name) && password.length() > 3) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            UserReq userReq = new UserReq(name, email, password, password_confirmation);
            addUser(userReq);
        }

    }

    private void addUser(UserReq userReq) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cadastrando usuário...");
        dialog.setCancelable(false);
        dialog.show();

        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getApplicationContext(), gson);
        UserInterface req = globalRetrofit.getRetrofit().create(UserInterface.class);

        Call<User> addUser = req.addUser(userReq);

        addUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int code = response.code();
                if (code == 201) {
                    User user = response.body();
                    if (user.getId() > 0 && user.getName() != null) {
                        Notification.notify(getApplicationContext(), user.getName() + " cadastrado, aguarde desbloqueio!", 0);

                        Intent i = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Notification.notify(getApplicationContext(), "Não foi possível salvar, tente novamente", 0);
                    }
                } else if (code == 200) {
                    Notification.notify(getApplicationContext(), "Já cadastrado", 0);
                } else {
                    Notification.notify(getApplicationContext(), "Holve uma falha, tente novamente", 0);
                }

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

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
