package spcore.api.modules;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import spcore.api.SpCoreModule;

import java.io.IOException;

public class AppsModule extends SpCoreModule {
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
}
