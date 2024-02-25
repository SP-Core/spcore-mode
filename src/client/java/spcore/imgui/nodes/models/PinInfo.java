package spcore.imgui.nodes.models;

import spcore.imgui.nodes.enums.PinKind;
import spcore.imgui.nodes.enums.PinType;

public class PinInfo {
    public String name;
    public PinType type;
    public PinKind kind;
    public PinInfo(String name, PinType type){
        this.name = name;
        this.type = type;
        this.kind = PinKind.Input;
    }
}
