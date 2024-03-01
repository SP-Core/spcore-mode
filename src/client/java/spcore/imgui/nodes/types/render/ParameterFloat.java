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
        node.addInput(new PinInfo("input", PinType.Parameter));
        node.addOutput(new PinInfo("value", PinType.Float));
        node.addOutput(new PinInfo("output", PinType.Parameter));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        var v = new ParameterValue<Float>();
        if(!node.containsInputValue("id")){
            outputs.put("value", 0.0f);
            outputs.put("output", null);
            return outputs;
        }
        var id = node.getInputValue("id");
        v.id = id;
        if(inputs.contains("input")){
            var inputMap = (HashMap<String, Object>)inputs.process("input");

            v.value = (Float)inputMap.get(id);
        }
        else{
            if(inputs.contains("value")){
                v.value = (float)inputs.process("value");
            }
            else if(node.containsInputValue("value")){
                v.value = Float.parseFloat(node.getInputValue("value"));
            }
        }




        outputs.put("value", v.value);
        outputs.put("output", v);
        return outputs;
    }
}
