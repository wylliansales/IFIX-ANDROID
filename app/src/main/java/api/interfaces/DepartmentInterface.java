package api.interfaces;

import java.util.List;

import api.Response.Department;
import api.Response.Equipment;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DepartmentInterface {

    @GET("departments/all")
    Call<List<Department>> getDepartments();

    @GET("departments/equipments/{id}")
    Call<List<Equipment>> getEquipments(@Path("id") int department_id);

}
