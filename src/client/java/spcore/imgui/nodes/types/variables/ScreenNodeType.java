package spcore.imgui.nodes.types.variables;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class ScreenNodeType extends AbstractNodeType {
    @Override
    public String getName() {
        return "screen vars";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

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
        var outputs = new HashMap<String, Object>();
        outputs.put("width", inputs.render.context.screen.width);
        outputs.put("height", inputs.render.context.screen.height);

        return outputs;
    }
}
