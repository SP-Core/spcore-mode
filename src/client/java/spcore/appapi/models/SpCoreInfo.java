package spcore.appapi.models;

import java.util.Objects;

public class SpCoreInfo {
    public SpCoreManifest manifest;
    public String exe;
    public DevOptions dev;
    public String absolute;
    @Override
    public int hashCode() {
        var h1 = Objects.hash(manifest, exe, absolute);
        if(dev != null && dev.views != null){
            h1 = Objects.hash(h1, dev.views);
        }
        return h1;
    }

    public static class DevOptions{
        public String views;
    }
}
