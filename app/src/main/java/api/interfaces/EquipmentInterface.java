package api.interfaces;
import java.util.List;
import api.Response.Equipment;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface EquipmentInterface {
    @GET("equipments/sector/{id}")
    Call<List<Equipment>> getAll(@Path("id") int sector_id);
}