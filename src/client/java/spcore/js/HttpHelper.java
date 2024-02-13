package spcore.js;

import okhttp3.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import spcore.GlobalContext;
import spcore.api.AuthContext;
import spcore.api.delegates.ResponseDelegate;
import spcore.api.delegates.UrlBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHelper {

    public static final RequestBody NoneBody = RequestBody.create(null, new byte[]{});
    private static final OkHttpClient client = new OkHttpClient();

    public static Call CreateGetCall(String url){
        return client.newCall(CreateBaseRequest(url)
                .get()
                .build());
    }

    public static Call CreatePostCall(String url, RequestBody body){
        return client.newCall(CreateBaseRequest(url)
                .post(body)
                .build());
    }

    public static Call CreatePutCall(String url, RequestBody body){
        return client.newCall(CreateBaseRequest(url)
                .put(body)
                .build());
    }

    public static void Send(Call call, ResponseDelegate responseDelegate){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                GlobalContext.LOGGER.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseDelegate.invoke(call, response);
                }
                finally {
                    response.close();
                }
            }
        });
    }

    private static Request.Builder CreateBaseRequest(String url){
        return new Request.Builder()
                .url(url)
                .header("code", AuthContext.getCode());
    }


}
