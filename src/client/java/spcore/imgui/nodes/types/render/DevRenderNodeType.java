package spcore.imgui.nodes.types.render;

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

public class DevRenderNodeType extends AbstractNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("show", PinType.Bool));
        node.addInput(new PinInfo("component", PinType.Component));

        return node;
    }


    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = new ViewComponent();
        view.setWidth(inputs.render.context.scope.width);
        view.setHeight(inputs.render.context.scope.height);
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
