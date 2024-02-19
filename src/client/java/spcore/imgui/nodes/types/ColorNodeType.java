package spcore.imgui.nodes.types;

import imgui.ImColor;
import org.joml.Vector4f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;

public class ColorNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "color";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "color",
                        PinType.Vector4));

        node.outputs.add(
                new Pin(context.nextId(),
                        "vector",
                        PinType.Vector4));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
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
