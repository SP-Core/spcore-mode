package spcore.api.delegates;

import org.apache.http.client.utils.URIBuilder;

public interface UrlBuilder {
    public void invoke(URIBuilder builder);
}
