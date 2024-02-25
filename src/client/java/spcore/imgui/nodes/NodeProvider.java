package spcore.imgui.nodes;

import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;

import java.io.IOException;
import java.util.List;

public interface NodeProvider {
    public String name();
    public boolean save(ViewFile viewFile);

    public ViewFile load() throws IOException;

    static class ViewFile{
        public List<Node> nodes;
        public List<Link> links;
        public int lastId;

    }
}
