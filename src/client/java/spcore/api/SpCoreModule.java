package spcore.api;

import okhttp3.*;
import org.apache.http.client.utils.URIBuilder;
import spcore.GlobalContext;
import spcore.api.delegates.ResponseDelegate;
import spcore.api.delegates.UrlBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class SpCoreModule {

    protected final RequestBody NoneBody = RequestBody.create(null, new byte[]{});
    protected final OkHttpClient client = new OkHttpClient();

    protected void Send(Call call, ResponseDelegate responseDelegate){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                GlobalContext.LOGGER.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseDelegate.invoke(call, response);
            }
        });
    }

    protected Request.Builder CreateGetRequest(UrlBuilder actionBuilder){
        return CreateBaseRequest(actionBuilder)
                .get();
    }

    protected Request.Builder CreatePostRequest(UrlBuilder actionBuilder, RequestBody body){
        return CreateBaseRequest(actionBuilder)
                .post(body);
    }

    protected Request.Builder CreatePutRequest(UrlBuilder actionBuilder, RequestBody body){
        return CreateBaseRequest(actionBuilder)
                .put(body);
    }

    protected Request.Builder CreateBaseRequest(UrlBuilder actionBuilder){
        var url = GetUrl(actionBuilder);
        return new Request.Builder()
                .url(url)
                .header("code", AuthContext.getCode());
    }

    private URL GetUrl(UrlBuilder actionBuilder){
        URIBuilder builder;
        try {
            builder = new URIBuilder(GlobalContext.API_URL);
            actionBuilder.invoke(builder);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addParameter("UserName", GlobalContext.getUserName());
        try {
            return builder.build().toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
