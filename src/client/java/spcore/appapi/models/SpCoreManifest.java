package spcore.appapi.models;

import java.util.Objects;

public class SpCoreManifest {
    public String appId;
    public String name;
    public String lifetime;
    public String version;
    public String server_domain;

    public String host_access;
    @Override
    public int hashCode() {
        return Objects.hash(appId, name, lifetime, version, server_domain);
    }
}
