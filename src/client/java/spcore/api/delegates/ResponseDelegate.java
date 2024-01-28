package spcore.api.delegates;

import okhttp3.Call;
import okhttp3.Response;

public interface ResponseDelegate {
    public void invoke(Call call, Response response);
}
