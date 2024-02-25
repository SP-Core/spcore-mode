package spcore.imgui.nodes.types.literal;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.*;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class IntNodeType extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("value", PinType.Int));
        node.addOutput(new PinInfo("value", PinType.Int));

        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        if(inputs.contains("value")){
            outputs.put("value", inputs.process("value"));
        }
        else if(node.containsInputValue("value")){
            outputs.put("value", Integer.parseInt(node.getInputValue("value")));
        }
        else{
            outputs.put("value", 0);
        }

        return outputs;
    }
}
