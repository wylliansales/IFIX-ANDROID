package api.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import api.exception.ValidationException;

public class GlobalDes implements JsonDeserializer<Object>{

    private Type typeResponse;

    public GlobalDes(Type type) {
        this.typeResponse = type;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement je = json.getAsJsonObject();

        if(json.getAsJsonObject().get("data") != null) {
            je = json.getAsJsonObject().get("data");
        } else if(json.getAsJsonObject().get("error") != null){
            return new Gson().fromJson(je, ValidationException.class);
        }
        return new Gson().fromJson(je, this.typeResponse);
    }
}
