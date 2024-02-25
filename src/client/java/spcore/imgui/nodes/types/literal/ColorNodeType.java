package spcore.imgui.nodes.types.literal;

import imgui.ImColor;
import org.joml.Vector4f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class ColorNodeType extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("color", PinType.Vector4));
        node.addOutput(new PinInfo("vector", PinType.Vector4));

        return node;
    }


    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        Vector4f vector4;
        if(inputs.contains("color")){
            vector4 = (Vector4f)inputs.process("color");
        }
        else if(node.containsInputValue("color")){
            vector4 = Vector4ValueInput.parse(node.getInputValue("color"));
        }
        else{
            throw new RuntimeException();
        }

        outputs.put("vector", vector4);

        return outputs;
    }


}
