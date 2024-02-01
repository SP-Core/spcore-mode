package spcore.api.modules;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import okhttp3.*;
import spcore.api.SpCoreModule;
import spcore.api.delegates.PatternDelegate;
import spcore.api.models.DocumentPattern;

import java.io.IOException;
import java.util.Objects;

public class PatternModule extends SpCoreModule {

    public void GetPattern(String patternName, PatternDelegate callback){
        var request = CreateGetRequest(b -> {
            b.setPath("/api/v1/patterns/get");
            b.addParameter("patternName", patternName);
        });
        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                callback.Invoke(false, null);
            }
            String jsonData = null;
            try {
                jsonData = Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Gson gson = new Gson();
            ObjectMapper mapper = new ObjectMapper(gson);
            var pattern = mapper.readValue(jsonData, DocumentPattern.class);
            callback.Invoke(true, pattern);
        });
    }

    public void PutPattern(DocumentPattern pattern, PatternDelegate callback){
        ObjectMapper mapper = new ObjectMapper(new Gson());
        var json = mapper.writeValueAsString(pattern);
        var body = RequestBody.create(MediaType.get("application/json"), json);
        var request = CreatePutRequest(b -> {
            b.setPath("/api/v1/patterns/put");
        }, body);

        Send(client.newCall(request.build()), (c, response) -> {
            if(!response.isSuccessful()){
                callback.Invoke(false, null);
            }

            callback.Invoke(true, pattern);
        });
    }
}
