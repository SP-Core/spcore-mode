package spcore.imgui.nodes.types.render;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.ParameterValue;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class ParameterComponent extends AbstractNodeType {
    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("component", PinType.Component));
        node.addInput(new PinInfo("input", PinType.Parameter));
        node.addOutput(new PinInfo("component", PinType.Component));
        node.addOutput(new PinInfo("output", PinType.Parameter));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var v = new ParameterValue<Renderable>();
        var input = inputs.process("component");
        if(input == null){
            v.value = new ViewComponent();
        }
        else{
            v.value = (Renderable)input;
        }

        if(node.containsInputValue("id")){
            v.id = node.getInputValue("id");
        }

        outputs.put("output", v);
        return outputs;
    }
}
