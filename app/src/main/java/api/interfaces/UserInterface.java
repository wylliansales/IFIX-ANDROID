package api.interfaces;

import api.requests.Credential;
import api.Response.Token;
import api.Response.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInterface {

    @POST("oauth/token")
    Call<Token> getToken(@Body Credential credential);

    @POST("users")
    Call<User> addUser(@Body User user);

    @GET("users/user/login")
    Call<User> getUserLogin();

    @PUT("users")
    Call<User> updateUser(@Body User user);

}
