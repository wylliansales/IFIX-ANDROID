package br.com.ifix.ifix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import api.HttpGlobalRetrofit;
import api.Response.Token;
import api.deserializers.TokenDes;
import api.interfaces.UserInterface;
import api.requests.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    ProgressDialog dialog;
//    SharedPreferences user_credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);

//        user_credentials = getSharedPreferences("user_credentials", MODE_PRIVATE);


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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button button_create_user = (Button) findViewById(R.id.button_create_user);
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
           // mAuthTask = new UserLoginTask(email, password);
          //  mAuthTask.execute((Void) null);
          //  Intent i = new Intent(LoginActivity.this, HomeActivity.class);
           // startActivity(i);
          //  finish();
            this.generateToken();

        }
    }

    private void generateToken(){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Autenticando aplicação...");
        dialog.setCancelable(false);
        dialog.show();

        final ApiClient credential =  new ApiClient(
                getString(R.string.grant_type),
                Integer.parseInt(getString(R.string.client_id)),
                getString(R.string.client_secret),
                this.mEmailView.getText().toString(),
                this.mPasswordView.getText().toString(),
                getString(R.string.scope));


        Gson gson = new GsonBuilder().registerTypeAdapter(Token.class, new TokenDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(gson);
        UserInterface req = globalRetrofit.getRetrofit().create(UserInterface.class);

        Call<Token> generateToken = req.getToken(credential);


        generateToken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(dialog.isShowing())
                    dialog.dismiss();

                int code = response.code();
                if(code == 200) {
                    Token token = response.body();

                    if(token != null){
//                        SharedPreferences.Editor editor = user_credentials.edit();
//                        editor.putString("username", credential.getUsername());
//                        editor.putString("password", credential.getPassword());
//                        editor.putString("access_token", token.getAccess_token());
//                        GregorianCalendar hoje = new GregorianCalendar();
//                        hoje.setTime(new Date());
//                        hoje.add(Calendar.DAY_OF_MONTH, token.getExpires_in()/86400);
                    }
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();

                    Toast.makeText(getBaseContext(), "Token de acesso " + token.getExpires_in(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Falha: " + String.valueOf(code), Toast.LENGTH_LONG).show();
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

