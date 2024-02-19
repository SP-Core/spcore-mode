package spcore.imgui.nodes.types;

import imgui.ImColor;
import org.joml.Vector2f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MixNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Mix";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(),
                ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "parent",
                        PinType.Component));

        node.inputs.add(
                new Pin(context.nextId(),
                        "components",
                        PinType.Components));

        node.outputs.add(
                new Pin(context.nextId(),
                        "output",
                        PinType.Component));


        context.nodes.add(node);

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        var parent = (Renderable)inputs.process("parent");
        if(parent == null)
        {
            outputs.put("output", new ViewComponent());
            return outputs;
        }

        var components = (List<Renderable>)inputs.process("components");
        if(components == null){
            outputs.put("output", parent);
            return outputs;
        }

        for (var child: components
             ) {
            parent.addChild(child);
        }

        outputs.put("output", parent);
        return outputs;
    }
}
