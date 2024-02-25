package spcore.imgui.nodes.types.literal;

import imgui.ImColor;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

public class VectorNodeType extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("x", PinType.Float));
        node.addInput(new PinInfo("y", PinType.Float));
        node.addInput(new PinInfo("z", PinType.Float));
        node.addInput(new PinInfo("w", PinType.Float));

        node.addOutput(new PinInfo("vector2", PinType.Vector2));
        node.addOutput(new PinInfo("vector3", PinType.Vector3));
        node.addOutput(new PinInfo("vector4", PinType.Vector4));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        Vector4f vector = new Vector4f();
        if(inputs.contains("x")){
            vector.x = (Float)inputs.process("x");
        }
        else if(node.containsInputValue("x")){
            vector.x = Float.parseFloat(node.getInputValue("x"));
        }

        if(inputs.contains("y")){
            vector.y = (Float)inputs.process("y");
        }
        else if(node.containsInputValue("y")){
            vector.y = Float.parseFloat(node.getInputValue("y"));
        }

        if(inputs.contains("z")){
            vector.z = (Float)inputs.process("z");
        }
        else if(node.containsInputValue("z")){
            vector.z = Float.parseFloat(node.getInputValue("z"));
        }

        if(inputs.contains("w")){
            vector.w = (Float)inputs.process("w");
        }
        else if(node.containsInputValue("w")){
            vector.w = Float.parseFloat(node.getInputValue("w"));
        }


        outputs.put("vector2", new Vector2f(vector.x, vector.y));
        outputs.put("vector3", new Vector3f(vector.x, vector.y, vector.z));
        outputs.put("vector4", vector);
        return outputs;
    }
}
