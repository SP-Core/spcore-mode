package spcore.imgui.nodes.providers;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import spcore.imgui.nodes.NodeProvider;
import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;

import java.util.Arrays;
import java.util.List;

public class ProductionProvider implements NodeProvider {

    private final List<Node> nodes;
    private final List<Link> links;
    private final int lastId;
    private final String viewName;

    public ProductionProvider(String viewName, List<Node> nodes, List<Link> links, int lastId) {
        this.viewName = viewName;
        this.nodes = nodes;
        this.links = links;
        this.lastId = lastId;
    }

    @Override
    public String name() {
        return viewName;
    }

    @Override
    public boolean save(ViewFile viewFile) {
        return false;
    }

    @Override
    public ViewFile load() {
        var f = new ViewFile();
        f.nodes = nodes;
        f.links = links;
        f.lastId = lastId;
        return f;
    }

    @Override
    public String getBlueprintPath() {
        return "empty.json";
    }
}
