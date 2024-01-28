package spcore.fabric.spcore;

import spcore.fabric.spcore.interfaces.ApiCallback;
import spcore.fabric.spcore.interfaces.PatternCallback;
import spcore.fabric.spcore.interfaces.PriceListCallback;
import spcore.fabric.spcore.models.DocumentPattern;
import spcore.fabric.spcore.models.PriceList;
import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class SpCoreApi {

    private final static String BaseUrl = "https://spcore.ru/";

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
                SpCoreApiContext.SaveSecret();
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

    public static void TreeClick(String message, Integer stage){
        var request = CreateBaseGetRequest("api/v1/tree/click?message=" +message + "&state=" + stage + "&");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибки
                //e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    return;
                }
                String responseBody = response.body().string();
                response.close();
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.inGameHud.setOverlayMessage(Text.of(responseBody), false);
            }
        });

    }

    public static void MCProfiles(String uuid){
        var request = CreateBasePostRequest("api/v1/app/mc-profiles?uuid=" + uuid + "&");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибки
                //e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


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

    public static void GetPriceList(PriceListCallback success, ApiCallback error){
        var request =
                CreateBaseGetRequest("api/v1/tree/price-list?");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                error.invoke();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    error.invoke();
                    return;
                }
                var jsonData = response.body().string();
                Gson gson = new Gson();
                ObjectMapper mapper = new ObjectMapper(gson);
                var priceList = mapper.readValue(jsonData, PriceList.class);
                success.Invoke(priceList);
            }
        });
    }

    public static void PayMulticlickLevel(ApiCallback success, ApiCallback error){
        var request =
                CreateBasePostRequest("api/v1/tree/pay-multiclick?");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                error.invoke();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    error.invoke();
                    return;
                }
                success.invoke();
            }
        });
    }

    public static void PayEnergyLimitPrice(ApiCallback success, ApiCallback error){
        var request =
                CreateBasePostRequest("api/v1/tree/pay-energy-limit?");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                error.invoke();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    error.invoke();
                    return;
                }
                success.invoke();
            }
        });
    }

    public static void PayRechangingSpeed(ApiCallback success, ApiCallback error){
        var request =
                CreateBasePostRequest("api/v1/tree/pay-rechanging-speed?");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                error.invoke();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    error.invoke();
                    return;
                }
                success.invoke();
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