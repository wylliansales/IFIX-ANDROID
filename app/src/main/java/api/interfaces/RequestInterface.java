package api.interfaces;

import java.util.List;
import api.Response.Request;
import api.Response.Status;
import models.RequestReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestInterface {

    @POST("requests")
    Call<Request> addRequest(@Body RequestReq request);

    @GET("requests/user")
    Call<List<Request>> getRequests();

    @GET("requests/open")
    Call<List<Request>> getRequestOpen();

    @GET("requests/closed")
    Call<List<Request>> getRequestClosed();

    @GET("requests/news")
    Call<List<Request>> getRequestNotStart();

    @GET("requests/status/{id}")
    Call<List<Status>> getStatus(@Path("id") int request_id);


}