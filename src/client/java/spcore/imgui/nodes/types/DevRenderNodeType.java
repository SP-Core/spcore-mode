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

public class DevRenderNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Dev render";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "show",
                        PinType.Bool));

        node.inputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = new ViewComponent();
        view.setWidth(inputs.render.context.screen.width);
        view.setHeight(inputs.render.context.screen.height);
        boolean show = false;
        if(node.containsInputValue("show")){
            show = Boolean.parseBoolean(node.getInputValue("show"));
        }
        var components = inputs.processes("component", Renderable.class);
        for (var component: components
             ) {

            if(show){
                view.addChild(component);
            }
        }
        outputs.put("out", view);

        return outputs;
    }
}
