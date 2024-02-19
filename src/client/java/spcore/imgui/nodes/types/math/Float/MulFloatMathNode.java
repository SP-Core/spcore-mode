package spcore.imgui.nodes.types.math.Float;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class MulFloatMathNode extends AbstractNodeType {
    @Override
    public String getName() {
        return "Mul float";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(),
                ImColor.floatToColor(255, 128, 128));
        node.type = NodeType.Blueprint;
        node.inputs.add(
                new Pin(context.nextId(),
                        "var1",
                        PinType.Float));

        node.inputs.add(
                new Pin(context.nextId(),
                        "var2",
                        PinType.Float));

        node.outputs.add(
                new Pin(context.nextId(),
                        "value",
                        PinType.Float));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        float var1 = 1;
        if(inputs.contains("var1")){
            var1 = (float)inputs.process("var1");
        }
        else if(node.containsInputValue("var1")){
            var1 = Float.parseFloat(node.getInputValue("var1"));
        }

        float var2 = 1;
        if(inputs.contains("var2")){
            var2 = (float)inputs.process("var2");
        }
        else if(node.containsInputValue("var2")){
            var2 = Float.parseFloat(node.getInputValue("var2"));
        }

        outputs.put("value", var1 * var2);
        return outputs;
    }
}
