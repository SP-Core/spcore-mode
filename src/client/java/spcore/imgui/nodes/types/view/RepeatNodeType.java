package spcore.imgui.nodes.types.view;

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
import spcore.view.render.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepeatNodeType extends AbstractNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);

        node.addInput(new PinInfo("component", PinType.Component));
        node.addInput(new PinInfo("count", PinType.Int));

        node.addOutput(new PinInfo("result", PinType.Components));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        var outputs = new HashMap<String, Object>();

        int count = 1;
        if(inputs.contains("count")){
            count = (int)inputs.process("count");
        }
        else if(node.containsInputValue("count")){
            count = Integer.parseInt(node.getInputValue("count"));
        }

        List<Renderable> components = new ArrayList<>();
        for(var i = 0; i < count; i++){
            var component = (Renderable)inputs.process("component");
            if(component != null){
                components.add(component);
            }

        }

        outputs.put("result", components);

        return outputs;
    }
}
