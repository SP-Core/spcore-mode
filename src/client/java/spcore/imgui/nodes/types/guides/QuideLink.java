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

public class QuideLink extends AbstractNodeType {
    @Override
    public String getName() {
        return "Quide link";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "vertical",
                        PinType.String));

        node.inputs.add(
                new Pin(context.nextId(),
                        "horizontal",
                        PinType.String));

        node.outputs.add(
                new Pin(context.nextId(),
                        "vector",
                        PinType.Vector2));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var vector = new Vector2f();
        outputs.put("vector", vector);

        if(inputs.contains("vertical")){
            var v = (String) inputs.process("vertical");
            if(inputs.data.verticals.containsKey(v)){
                vector.x = inputs.data.verticals.get(v);
            }
        }
        else if(node.containsInputValue("vertical")){
            var v = node.getInputValue("vertical");
            if(inputs.data.verticals.containsKey(v)){
                vector.x = inputs.data.verticals.get(v);
            }

        }

        if(inputs.contains("horizontal")){
            var v = (String) inputs.process("horizontal");
            if(inputs.data.horizontals.containsKey(v)){
                vector.y = inputs.data.horizontals.get(v);
            }
        }
        else if(node.containsInputValue("horizontal")){
            var v = node.getInputValue("horizontal");
            if(inputs.data.horizontals.containsKey(v)){
                vector.y = inputs.data.horizontals.get(v);
            }

        }

        return outputs;
    }


}
