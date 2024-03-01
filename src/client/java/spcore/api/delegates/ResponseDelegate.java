package spcore.api.delegates;

import okhttp3.Call;
import okhttp3.Response;

public interface ResponseDelegate {
    void invoke(Call call, Response response);
}
