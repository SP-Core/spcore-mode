package spcore.appapi.models;

import java.util.Objects;

public class SpCoreInfo {
    public SpCoreManifest manifest;
    public String exe;
    public String absolute;
    @Override
    public int hashCode() {
        return Objects.hash(manifest, exe, absolute);
    }
}
