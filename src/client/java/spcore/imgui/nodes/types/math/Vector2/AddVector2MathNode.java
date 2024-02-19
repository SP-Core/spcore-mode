package spcore.imgui.nodes.types.math.Vector2;

import imgui.ImColor;
import org.joml.Vector2f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class AddVector2MathNode extends AbstractNodeType {
    @Override
    public String getName() {
        return "Add vector2";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(),
                ImColor.floatToColor(255, 128, 128));
        node.type = NodeType.Blueprint;
        node.inputs.add(
                new Pin(context.nextId(),
                        "var1",
                        PinType.Vector2));

        node.inputs.add(
                new Pin(context.nextId(),
                        "var2",
                        PinType.Vector2));

        node.outputs.add(
                new Pin(context.nextId(),
                        "value",
                        PinType.Vector2));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var var1 = (Vector2f)inputs.process("var1");
        var var2 = (Vector2f)inputs.process("var2");

        if(var1 == null && var2 == null){
            outputs.put("value", new Vector2f(0, 0));
        }
        else if(var1 != null && var2 == null){
            outputs.put("value", var1);
        }
        else if(var1 == null && var2 != null){
            outputs.put("value", var2);
        }
        else{
            outputs.put("value", var1.add(var2));
        }
        return outputs;
    }
}

