package spcore.imgui.nodes.types;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class RenderNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "render";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "layer",
                        PinType.Int));

        node.inputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var input = inputs.process("component");
        if(input == null){
            outputs.put("out", new ViewComponent());
            return outputs;
        }
        var component = (Renderable)input;

        Object layerValue = inputs.process("layer");
        String layer;
        if(layerValue == null){
            if(node.containsInputValue("layer")){
                layer = node.getInputValue("layer");
            }
            else{
                layer = "0";
            }
        }
        else {
            layer = (String)layerValue;
        }

        component.getStyle().styles.put(Renderable.RenderableStyles.LAYER, layer);
        outputs.put("out", component);
        return outputs;
    }
}
