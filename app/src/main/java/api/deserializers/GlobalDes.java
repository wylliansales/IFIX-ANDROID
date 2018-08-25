package api.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import api.Response.ResponseGlobal;
import api.exception.ValidationException;

public class GlobalDes implements JsonDeserializer<Object>{


    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement je = json.getAsJsonObject();


        return new Gson().fromJson(je, ResponseGlobal.class);
    }
}
