package spcore.api;

import spcore.api.modules.*;

public class SpCoreApi {
    public static final AppsModule APPS = new AppsModule();
    public static final AuthModule AUTH = new AuthModule();
    public static final TreeModule TREE = new TreeModule();
    public static final PatternModule PATTERN = new PatternModule();
    public static final CoreGetModule CORE_GET = new CoreGetModule();
}
