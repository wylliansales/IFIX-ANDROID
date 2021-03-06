package api.interfaces;

import api.requests.ApiClient;
import api.Response.Token;
import api.Response.User;
import api.requests.UserReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInterface {

    @POST("oauth/token")
    Call<Token> getToken(@Body ApiClient client);

    @POST("users")
    Call<User> addUser(@Body UserReq user);

    @GET("users/user/login")
    Call<User> getUserLogin();

    @PUT("users")
    Call<User> updateUser(@Body User user);

}
