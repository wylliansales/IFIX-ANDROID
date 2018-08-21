package api.interfaces;

import java.util.List;

import api.Response.Sector;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SectorInterface {

    @GET("sectors/all")
    Call<List<Sector>> getAll();
}
