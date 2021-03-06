package api.deserializers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import api.Response.Department;

public class DepartmentDes implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement je = json.getAsJsonObject();

        if(json.getAsJsonObject().get("data") != null) je = json.getAsJsonObject().get("data");

        return new Gson().fromJson(je, Department.class);
    }
}
