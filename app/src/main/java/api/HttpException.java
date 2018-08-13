package api;

import android.util.Log;
import com.google.gson.Gson;
import retrofit2.Response;

public class HttpException {

    public static boolean isSuccess(Response response) {
        if(!response.isSuccessful()){
            Gson gson = new Gson();
            ValidationException ex = gson.fromJson(response.errorBody().charStream(), ValidationException.class);
            System.out.println(ex.getCode());
            Log.e("Error",ex.getMessage());
            return false;
        } else if(response.body().getClass() == ValidationException.class){
            ValidationException e = (ValidationException) response.body();
            if(e != null)
                if(e.getError().equalsIgnoreCase("validation_exception")) {
                    Log.e(e.getError(),e.getError_description().toString());
                    return false;
                }
        }
        return true;
    }
}
