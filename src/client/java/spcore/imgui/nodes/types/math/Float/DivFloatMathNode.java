package spcore.imgui.nodes.types.math.Float;

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

import java.util.HashMap;

public class DivFloatMathNode extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("var1", PinType.Float));
        node.addInput(new PinInfo("var2", PinType.Float));

        node.addOutput(new PinInfo("value", PinType.Float));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        float var1 = 1;
        if(inputs.contains("var1")){
            var1 = (float)inputs.process("var1");
        }
        else if(node.containsInputValue("var1")){
            var1 = Float.parseFloat(node.getInputValue("var1"));
        }

        float var2 = 1;
        if(inputs.contains("var2")){
            var2 = (float)inputs.process("var2");
        }
        else if(node.containsInputValue("var2")){
            var2 = Float.parseFloat(node.getInputValue("var2"));
        }

        outputs.put("value", var1 / var2);
        return outputs;
    }
}
