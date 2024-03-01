package spcore.api.delegates;

import org.apache.http.client.utils.URIBuilder;

public interface UrlBuilder {
    void invoke(URIBuilder builder);
}
