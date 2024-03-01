package spcore.engine.models;

import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;

import java.util.List;

public class ViewController {
    public String name;
    public List<Node> nodes;
    public List<Link> links;
    public int lastId;

    public boolean gameHub = false;
    public transient int index;
}
