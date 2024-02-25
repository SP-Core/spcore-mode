package spcore.imgui.nodes.types.storage;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class StorageValueNodeType extends AbstractNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("int", PinType.Int));
        node.addInput(new PinInfo("float", PinType.Float));

        node.addOutput(new PinInfo("int", PinType.Int));
        node.addOutput(new PinInfo("float", PinType.Float));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        if(inputs.data.statics.containsKey(node.id)){
            return inputs.data.statics.get(node.id);
        }

        HashMap<String, Object> outputs = new HashMap<>();

        if(inputs.contains("int")){
            outputs.put("int", inputs.process("int"));
        }
        else if(node.containsInputValue("int")){
            outputs.put("int", Integer.parseInt(node.getInputValue("int")));
        }

        if(inputs.contains("float")){
            outputs.put("float", inputs.process("float"));
        }
        else if(node.containsInputValue("float")){
            outputs.put("float", Integer.parseInt(node.getInputValue("float")));
        }

        inputs.data.statics.put(node.id, outputs);
        return outputs;
    }
}
