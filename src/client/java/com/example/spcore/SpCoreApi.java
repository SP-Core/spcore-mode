package com.example.spcore;

import com.example.spcore.interfaces.ApiCallback;
import com.example.spcore.interfaces.PatternCallback;
import com.example.spcore.models.DocumentPattern;
import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class SpCoreApi {

    private final static String BaseUrl = "http://localhost:5021/";

    private final static RequestBody noneBody = RequestBody.create(null, new byte[]{});
    private final static OkHttpClient client = new OkHttpClient();

    public static void PostIsAuthorized(ApiCallback success, ApiCallback error){

        var request = CreateBasePostRequest("api/v1/crypto/isAuthByToken?");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                SpCoreApiContext.IsAuthorized = false;
                SpCoreApiContext.Code = null;

                error.invoke();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    SpCoreApiContext.IsAuthorized = false;
                    SpCoreApiContext.Code = null;
                    error.invoke();
                    return;
                }
                SpCoreApiContext.IsAuthorized = true;
                success.invoke();
            }
        });


    }

    public static void GetPattern(String patternName, PatternCallback callback){
        var request = CreateBaseGetRequest("api/v1/patterns/get?patternName=" + patternName + "&&");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.Invoke(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    callback.Invoke(false, null);
                }
                String jsonData = Objects.requireNonNull(response.body()).string();

                Gson gson = new Gson();
                ObjectMapper mapper = new ObjectMapper(gson);
                var pattern = mapper.readValue(jsonData, DocumentPattern.class);
                callback.Invoke(true, pattern);
            }
        });
    }

    public static void PutPattern(DocumentPattern pattern, PatternCallback callback){
        ObjectMapper mapper = new ObjectMapper(new Gson());
        var json = mapper.writeValueAsString(pattern);
        var request = CreateBasePutRequest("api/v1/patterns/put?", RequestBody.create(MediaType.get("application/json"), json));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.Invoke(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    callback.Invoke(false, null);
                }

                callback.Invoke(true, pattern);
            }
        });
    }


    private static Request CreateBasePostRequest(String partUrl){
        return new Request.Builder()
                .url(BaseUrl + partUrl + "UserName=" + SpCoreApiContext.UserName)
                .header("code", SpCoreApiContext.Code)
                .post(noneBody)
                .build();

    }

    private static Request CreateBaseGetRequest(String partUrl){
        return new Request.Builder()
                .url(BaseUrl + partUrl + "UserName=" + SpCoreApiContext.UserName)
                .header("code", SpCoreApiContext.Code)
                .build();

    }

    private static Request CreateBasePutRequest(String partUrl, RequestBody body){
        return new Request.Builder()
                .url(BaseUrl + partUrl + "UserName=" + SpCoreApiContext.UserName)
                .put(body)
                .header("code", SpCoreApiContext.Code)
                .build();

    }
}