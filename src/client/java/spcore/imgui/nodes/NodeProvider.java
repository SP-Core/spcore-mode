package spcore.imgui.nodes;

import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;

import java.io.IOException;
import java.util.List;

public interface NodeProvider {
    String name();
    boolean save(ViewFile viewFile);

    ViewFile load() throws IOException;

    public String getBlueprintPath();
    class ViewFile{
        public List<Node> nodes;
        public List<Link> links;
        public int lastId;

    }
}
