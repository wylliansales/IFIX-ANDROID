package api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpGlobalRetrofit {

    private final Retrofit retrofit;
    public static final String API_URL = "https://app-serve.herokuapp.com/api/";

    public HttpGlobalRetrofit(Gson gson) {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getInterceptor())
                .build();
    }
    public HttpGlobalRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor())
                .build();
    }

    private OkHttpClient getInterceptor() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpInterceptor(null))
                .build();
        return client;
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }

}
