package spcore.api.modules;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import okhttp3.RequestBody;
import spcore.api.SpCoreModule;
import spcore.api.delegates.PatternDelegate;
import spcore.api.delegates.ResponseDelegate;
import spcore.api.models.DocumentPattern;

import java.io.IOException;
import java.util.Objects;

public class CoreGetModule extends SpCoreModule {
    public void GetTemplate(String appName, ResponseDelegate delegate){
        var request = CreateGetRequest(b -> {
            b.setPath("/api/v1/coreget/create-template");
            b.addParameter("appName", appName);
        });

        Send(client.newCall(request.build()), delegate);
    }

    public void Publish(RequestBody body, ResponseDelegate delegate){
        var request = CreatePostRequest(b -> {
            b.setPath("/api/v1/coreget/publish");
        }, body);

        Send(client.newCall(request.build()), delegate);
    }

    public void Get(String pack, ResponseDelegate delegate){
        var request = CreateGetRequest(b -> {
            b.setPath("/api/v1/coreget/get");
            b.addParameter("package", pack);
        });

        Send(client.newCall(request.build()), delegate);
    }
}
