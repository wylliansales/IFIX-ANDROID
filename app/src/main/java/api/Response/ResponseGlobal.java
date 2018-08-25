package api.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResponseGlobal {

    private JsonElement data;

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
