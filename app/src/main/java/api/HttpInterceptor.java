package api;

import java.io.IOException;

import api.Response.Token;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {

    private Token token;

    public HttpInterceptor(Token token){
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder request = req.newBuilder();

        request.addHeader("Content-Type", "application/json");
        if(this.token != null) {
          request.addHeader("Authorization", "Bearer " + this.token.getAccess_token());
        }
        //request.addHeader("Accept", "application/json");
        Request newRequest = request.build();

        return chain.proceed(newRequest);
    }
}
