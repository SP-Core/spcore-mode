package spcore.imgui.nodes.types;

import com.mojang.datafixers.View;
import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;

import java.util.HashMap;

public class StorageResetNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Storage Reset";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        node.outputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        context.nodes.add(node);
        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        inputs.data.statics.clear();
        HashMap<String, Object> outputs = new HashMap<>();
        if(inputs.contains("component")){
            outputs.put("component", inputs.process("component"));
        }
        else{
            outputs.put("component", new ViewComponent());
        }

        return outputs;
    }
}
