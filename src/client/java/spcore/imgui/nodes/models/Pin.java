package spcore.imgui.nodes.models;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinKind;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.PinId;

public class Pin {
    public PinId id;
    public NodeId nodeId;
    public String pinName;
    public PinType type;

    public transient PinInfo innerPin;
    public Pin(int id, String pinName, PinType type){
        this.id = new PinId(id);
        this.pinName = pinName;
        this.type = type;
    }
}
