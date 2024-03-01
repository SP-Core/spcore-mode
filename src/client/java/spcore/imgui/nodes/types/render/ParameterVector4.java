package spcore.imgui.nodes.types.render;

import org.joml.Vector2f;
import org.joml.Vector4f;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.ParameterValue;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class ParameterVector4 extends AbstractNodeType {
    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("value", PinType.Vector4));
        node.addInput(new PinInfo("input", PinType.Parameter));
        node.addOutput(new PinInfo("value", PinType.Vector4));
        node.addOutput(new PinInfo("output", PinType.Parameter));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var v = new ParameterValue<Vector4f>();

        if(!node.containsInputValue("id")){
            outputs.put("value", new Vector4f());
            outputs.put("output", null);
            return outputs;
        }
        var id = node.getInputValue("id");
        v.id = id;

        if(inputs.contains("input")){
            var inputMap = (HashMap<String, Object>)inputs.process("input");

            v.value = (Vector4f) inputMap.get(id);
        }
        else{
            if(inputs.contains("value")){
                v.value = (Vector4f) inputs.process("value");
            }

        }




        outputs.put("value", v.value);
        outputs.put("output", v);
        return outputs;
    }
}
