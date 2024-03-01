package spcore.imgui.nodes.types.render;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.*;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class RenderNodeType extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("layer", PinType.Int));
        node.addInput(new PinInfo("component", PinType.Component));
        node.addInput(new PinInfo("outputs", PinType.Parameter));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var input = inputs.process("component");
        Renderable component;
        if(input == null){
            component = new ViewComponent();
        }
        else{
            component = (Renderable)input;
        }


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

        if(inputs.contains("outputs")){
            for (var p: inputs.processes("outputs", ParameterValue.class)
                 ) {
                if(p == null)
                    continue;

                component.getStyle().data.put(p.id, p.value);
            }
        }

        outputs.put("out", component);
        return outputs;
    }
}
