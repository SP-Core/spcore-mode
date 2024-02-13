package spcore.js;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class nativeService {
    public Exception exception(String message){
        return new Exception(message);
    }

    public float toFloat(int i){
        return (float)i;
    }

    public float toFloat(double i){
        return (float)i;
    }

    public Map<String, Object> jsonToMap(String json) {
        return new ObjectMapper(new Gson()).readValue(json, HashMap.class);
    }
}
