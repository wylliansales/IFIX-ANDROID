package api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import DB.DataBase;
import models.Credential;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpGlobalRetrofit {

    private final Retrofit retrofit;
    private final Context ctx;
    public static final String API_URL = "https://app-serve.herokuapp.com/api/";

    public HttpGlobalRetrofit(Context ctx, Gson gson) {
        this.ctx = ctx;
        retrofit = new Retrofit
                .Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getInterceptor())
                .build();

    }
    public HttpGlobalRetrofit(Context ctx) {
        this.ctx = ctx;
        retrofit = new Retrofit
                .Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor())
                .build();
    }

    private OkHttpClient getInterceptor() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpInterceptor(this.getToken()))
                .build();
        return client;
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }

    private String getToken() {
        DataBase db = new DataBase(this.ctx);
        Credential credential = db.getCredentials();
        return credential.getAccess_token() != null ? credential.getAccess_token() : "";
    }

}
