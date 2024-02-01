package spcore.api;

import spcore.api.modules.AppsModule;
import spcore.api.modules.AuthModule;
import spcore.api.modules.PatternModule;
import spcore.api.modules.TreeModule;

public class SpCoreApi {
    public static final AppsModule APPS = new AppsModule();
    public static final AuthModule AUTH = new AuthModule();
    public static final TreeModule TREE = new TreeModule();
    public static final PatternModule PATTERN = new PatternModule();
}
