package spcore.imgui.nodes.models;

import spcore.imgui.nodes.enums.PinKind;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.PinId;

public class Pin {
    public PinId id;
    public NodeId nodeId;
    public String name;
    public PinType type;
    public PinKind kind;
    public Pin(int id, String name, PinType type){
        this.id = new PinId(id);
        this.name = name;
        this.type = type;
        this.kind = PinKind.Input;
    }
}
