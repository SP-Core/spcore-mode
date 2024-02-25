package spcore.imgui.nodes.types.typeScript;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class OutSetterNodeType extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addOutput(new PinInfo("out", PinType.TypeScriptSetter));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        var outputs = new HashMap<String, Object>();

        String id = null;
        if(node.containsInputValue("id")){
            id = node.getInputValue("id");
        }
        outputs.put("out", id);
        return outputs;
    }
}
