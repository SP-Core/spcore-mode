package spcore.imgui.nodes.types.guides;

import imgui.ImColor;
import org.joml.Vector2f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class QuideSize extends AbstractNodeType {
    @Override
    public String getName() {
        return "Quide size";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "top",
                        PinType.String));

        node.inputs.add(
                new Pin(context.nextId(),
                        "left",
                        PinType.String));

        node.inputs.add(
                new Pin(context.nextId(),
                        "bottom",
                        PinType.String));

        node.inputs.add(
                new Pin(context.nextId(),
                        "right",
                        PinType.String));

        node.outputs.add(
                new Pin(context.nextId(),
                        "width",
                        PinType.Int));

        node.outputs.add(
                new Pin(context.nextId(),
                        "height",
                        PinType.Int));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;
        if(node.containsInputValue("top")){
            var v = node.getInputValue("top");
            if(inputs.data.horizontals.containsKey(v)){
                top = inputs.data.horizontals.get(v);
            }
        }

        if(node.containsInputValue("bottom")){
            var v = node.getInputValue("bottom");
            if(inputs.data.horizontals.containsKey(v)){
                bottom = inputs.data.horizontals.get(v);
            }
        }

        if(node.containsInputValue("left")){
            var v = node.getInputValue("left");
            if(inputs.data.verticals.containsKey(v)){
                left = inputs.data.verticals.get(v);
            }
        }

        if(node.containsInputValue("right")){
            var v = node.getInputValue("right");
            if(inputs.data.verticals.containsKey(v)){
                right = inputs.data.verticals.get(v);
            }
        }

        outputs.put("width", Math.abs(left - right));
        outputs.put("height", Math.abs(top - bottom));
        return outputs;
    }
}
