package spcore.imgui.nodes.models;

import spcore.appapi.models.SpCoreInfo;
import spcore.engine.ViewContext;
import spcore.engine.models.ViewsInfo;

import java.util.HashMap;

public class ViewScope {
    public float width;
    public float height;
    public ViewsInfo viewsInfo;
    public SpCoreInfo manifest;
    public ViewContext viewContext;

    public HashMap<String, Object> inputs = new HashMap<>();

    public ViewScope copy(){
        var v = new ViewScope();
        v.width = width;
        v.height = height;
        v.viewContext = viewContext;
        v.viewsInfo = viewsInfo;
        v.manifest = manifest;
        return v;
    }
}
