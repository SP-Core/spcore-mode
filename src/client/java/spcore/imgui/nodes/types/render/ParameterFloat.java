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

public class ParameterFloat extends AbstractNodeType {
    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("value", PinType.Float));
        node.addOutput(new PinInfo("value", PinType.Float));
        node.addOutput(new PinInfo("output", PinType.Parameter));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var v = new ParameterValue<Float>();

        if(inputs.contains("value")){
            v.value = (float)inputs.process("value");
        }
        else if(node.containsInputValue("value")){
            v.value = Float.parseFloat(node.getInputValue("value"));
        }

        outputs.put("output", v);
        return outputs;
    }
}
