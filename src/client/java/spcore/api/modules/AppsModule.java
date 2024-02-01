package spcore.api.modules;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import spcore.api.SpCoreModule;

import java.io.IOException;

public class AppsModule extends SpCoreModule {
    public void MCProfiles(String uuid){

        var request = CreatePostRequest(b ->{
            b.setPath("api/v1/app/mc-profiles");
            b.addParameter("uuid", uuid);
        }, NoneBody);


        Send(client.newCall(request.build()), (c, response) ->{

        });

    }
}
