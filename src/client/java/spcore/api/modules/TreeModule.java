package spcore.api.modules;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import spcore.api.SpCoreModule;
import spcore.api.delegates.Action;
import spcore.api.delegates.PriceListDelegate;
import spcore.api.models.PriceList;

import java.io.IOException;

public class TreeModule extends SpCoreModule {
    public void TreeClick(String message, Integer stage){

        var request = CreateGetRequest(b ->{
            b.setPath("/api/v1/tree/click");
            b.addParameter("message", message);
            b.addParameter("state", stage.toString());
        });


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                return;
            }
            String responseBody = null;
            try {
                responseBody = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.close();
            //вынести UI логику в отдельный модуль
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.inGameHud.setOverlayMessage(Text.of(responseBody), false);
        });
    }

    public void GetPriceList(PriceListDelegate success, Action error){

        var request = CreateGetRequest(b ->{
            b.setPath("api/v1/tree/price-list");
        });


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                error.invoke();
                return;
            }
            String jsonData = null;
            try {
                jsonData = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Gson gson = new Gson();
            ObjectMapper mapper = new ObjectMapper(gson);
            var priceList = mapper.readValue(jsonData, PriceList.class);
            success.Invoke(priceList);
        });

    }

    public void PayMulticlickLevel(Action success, Action error){
        var request = CreatePostRequest(b ->{
            b.setPath("api/v1/tree/pay-multiclick");
        }, NoneBody);


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                error.invoke();
                return;
            }
            success.invoke();
        });
    }

    public void PayEnergyLimitPrice(Action success, Action error){
        var request = CreatePostRequest(b ->{
            b.setPath("api/v1/tree/pay-energy-limit");
        }, NoneBody);


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                error.invoke();
                return;
            }
            success.invoke();
        });
    }

    public void PayRechangingSpeed(Action success, Action error){

        var request = CreatePostRequest(b ->{
            b.setPath("api/v1/tree/pay-rechanging-speed");
        }, NoneBody);


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                error.invoke();
                return;
            }
            success.invoke();
        });

    }

    public void UseEnergy(Action success, Action error){

        var request = CreatePostRequest(b ->{
            b.setPath("api/v1/tree/use-energy");
        }, NoneBody);


        Send(client.newCall(request.build()), (c, response) ->{
            if(!response.isSuccessful()){
                error.invoke();
                return;
            }
            success.invoke();
        });

    }

}
