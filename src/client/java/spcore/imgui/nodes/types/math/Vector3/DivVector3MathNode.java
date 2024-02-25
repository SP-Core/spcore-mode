package spcore.imgui.nodes.types.math.Vector3;

import imgui.ImColor;
import org.joml.Vector3f;
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

public class DivVector3MathNode extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("var1", PinType.Vector3));
        node.addInput(new PinInfo("var2", PinType.Vector3));

        node.addOutput(new PinInfo("value", PinType.Vector3));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var var1 = (Vector3f)inputs.process("var1");
        var var2 = (Vector3f)inputs.process("var2");

        if(var1 == null && var2 == null){
            outputs.put("value", new Vector3f(0, 0, 0));
        }
        else if(var1 != null && var2 == null){
            outputs.put("value", var1);
        }
        else if(var1 == null && var2 != null){
            outputs.put("value", var2);
        }
        else{
            outputs.put("value", var1.div(var2));
        }
        return outputs;
    }
}
