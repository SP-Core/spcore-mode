package spcore.imgui.nodes.types.guides;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class Quide extends AbstractNodeType {
    @Override
    public String getName() {
        return "Quide";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));
        node.inputs.add(
                new Pin(context.nextId(),
                        "id",
                        PinType.String));

        node.inputs.add(
                new Pin(context.nextId(),
                        "value",
                        PinType.Float));

        node.inputs.add(
                new Pin(context.nextId(),
                        "horizontal",
                        PinType.Bool));

        node.outputs.add(
                new Pin(context.nextId(),
                        "component",
                        PinType.Component));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = new ViewComponent();
        view.getStyle().styles
                .put(Renderable.RenderableStyles.PARENT_WIDTH, Integer.toString(inputs.render.context.screen.width));
        view.getStyle().styles
                .put(Renderable.RenderableStyles.PARENT_HEIGHT, Integer.toString(inputs.render.context.screen.height));

        outputs.put("component", view);
        view.setBackground("#8ccf4a4a");
        boolean horizontal = false;
        float value = 0;
        String id = "";
        if(node.containsInputValue("horizontal")){
            horizontal = Boolean.parseBoolean(node.getInputValue("horizontal"));
        }

        if(node.containsInputValue("id")){
            id = node.getInputValue("id");
        }

        if(node.containsInputValue("value")){
            value = Float.parseFloat(node.getInputValue("value"));
        }

        if(horizontal){
            view.setWidth(inputs.render.context.screen.width);
            view.setHeight(1);
            view.setY(Float.toString(value) + "%");
            inputs.data.horizontals.put(id, view.getY());
        }
        else{
            view.setHeight(inputs.render.context.screen.height);
            view.setWidth(1);
            view.setX(Float.toString(value) + "%");
            inputs.data.verticals.put(id, view.getX());
        }

        return outputs;
    }
}
