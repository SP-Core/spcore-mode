package spcore.imgui.nodes;

import imgui.type.ImBoolean;

public class NodeTab {
    public String name;
    public String fullPath;
    public ImBoolean value = new ImBoolean(true);
    public NodeTab(String name, String fullPath){
        this.name = name;
        this.fullPath = fullPath;
    }
}
