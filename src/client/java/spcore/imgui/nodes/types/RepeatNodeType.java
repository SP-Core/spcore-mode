package spcore.imgui.nodes.types;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.render.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepeatNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Repeat";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        node.inputs.add(
                new Pin(context.nextId(),
                        "count",
                        PinType.Int));

        node.outputs.add(
                new Pin(context.nextId(),
                        "result",
                        PinType.Components));

        context.nodes.add(node);
        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        var outputs = new HashMap<String, Object>();

        int count = 1;
        if(inputs.contains("count")){
            count = (int)inputs.process("count");
        }
        else if(node.containsInputValue("count")){
            count = Integer.parseInt(node.getInputValue("count"));
        }

        List<Renderable> components = new ArrayList<>();
        for(var i = 0; i < count; i++){
            var component = (Renderable)inputs.process("component");
            if(component != null){
                components.add(component);
            }

        }

        outputs.put("result", components);

        return outputs;
    }
}
