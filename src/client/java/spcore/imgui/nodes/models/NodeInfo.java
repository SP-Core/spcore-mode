package spcore.imgui.nodes.models;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeInfo {
    public final NodeType nodeType;

    public final List<PinInfo> inputs = new ArrayList<>();
    public final List<PinInfo> outputs = new ArrayList<>();
    private int inputIndex = 0;
    private int outputIndex = 0;
    public NodeInfo(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeInfo addInput(PinInfo pin){
        inputs.add(pin);
        pin.kind = PinKind.Input;
        return this;
    }

    public NodeInfo addOutput(PinInfo pin){
        outputs.add(pin);
        pin.kind = PinKind.Output;
        return this;
    }
}
