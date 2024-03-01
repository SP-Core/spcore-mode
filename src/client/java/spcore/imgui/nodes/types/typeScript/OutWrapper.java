package spcore.imgui.nodes.types.typeScript;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.ArrayList;
import java.util.HashMap;

public class OutWrapper extends AbstractNodeType {

    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("input", PinType.Component));
        node.addOutput(new PinInfo("output", PinType.Component));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        if(inputs.contains("input")){
            outputs.put("output", inputs.process("input"));
        }
        else{
            outputs.put("output", new ViewComponent());
        }

        String id = null;
        if(node.containsInputValue("id")){
            id = node.getInputValue("id");
        }

        if(id != null && !id.equals(""))
        {
            if(inputs.data.wrappers.containsKey(id)){
                inputs.data.wrappers.get(id).add((Renderable) outputs.get("output"));
            }
            else {
                inputs.data.wrappers.put(id, new ArrayList<>());
                inputs.data.wrappers.get(id).add((Renderable) outputs.get("output"));
            }
        }

        return outputs;
    }
}
