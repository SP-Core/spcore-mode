package spcore.imgui.nodes.types.guides;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class Guide extends AbstractNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("value", PinType.Float));
        node.addInput(new PinInfo("horizontal", PinType.Bool));
        node.addOutput(new PinInfo("component", PinType.Component));

        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = new ViewComponent();
        view.getStyle().styles
                .put(Renderable.RenderableStyles.PARENT_WIDTH, Float.toString(inputs.render.context.scope.width));
        view.getStyle().styles
                .put(Renderable.RenderableStyles.PARENT_HEIGHT, Float.toString(inputs.render.context.scope.height));

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
            view.setWidth(inputs.render.context.scope.width);
            view.setHeight(1);
            view.setY(Float.toString(value) + "%");
            inputs.data.horizontals.put(id, view.getY());
        }
        else{
            view.setHeight(inputs.render.context.scope.height);
            view.setWidth(1);
            view.setX(Float.toString(value) + "%");
            inputs.data.verticals.put(id, view.getX());
        }

        return outputs;
    }
}
