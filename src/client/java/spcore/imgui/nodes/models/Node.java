package spcore.imgui.nodes.models;

import imgui.ImColor;
import imgui.ImVec2;
import spcore.imgui.nodes.enums.NodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Node {
    public NodeId id;
    public String name;

    public String tabName;
    public List<Pin> inputs = new ArrayList<>();
    public List<Pin> outputs = new ArrayList<>();
    public HashMap<Integer, String> values = new HashMap<>();
    public HashMap<Integer, String> viewTabs = new HashMap<>();
    public int color;
    public NodeType type;

    public String state;
    public String saveState;

    public Node(int id, String name){
        this(id, name, ImColor.floatToColor(255, 255, 255));
    }

    public Node(int id, String name, int color){
        this.id = new NodeId(id);
        this.name = name;
        this.tabName = name;
        this.color = color;
        this.type = NodeType.Blueprint;
    }

    public Pin getInputPinFromName(String name){
        return inputs.stream().filter(p -> p.name.equals(name)).findFirst().get();
    }

    public boolean containsInputValue(String name){
        var pin = getInputPinFromName(name);
        return values.containsKey(pin.id.value);
    }

    public String getInputValue(String name){
        var pin = getInputPinFromName(name);
        return values.get(pin.id.value);
    }

    public Pin getOutputPinFromName(String name){
        return outputs.stream().filter(p -> p.name.equals(name)).findFirst().get();
    }
}
