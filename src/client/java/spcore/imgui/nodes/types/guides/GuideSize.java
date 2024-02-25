package spcore.imgui.nodes.types.guides;

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

public class GuideSize extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("top", PinType.String));
        node.addInput(new PinInfo("left", PinType.String));
        node.addInput(new PinInfo("bottom", PinType.String));
        node.addInput(new PinInfo("right", PinType.String));
        node.addOutput(new PinInfo("width", PinType.Float));
        node.addOutput(new PinInfo("height", PinType.Float));

        return node;
    }


    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        float top = 0;
        float bottom = 0;
        float left = 0;
        float right = 0;
        if(node.containsInputValue("top")){
            var v = node.getInputValue("top");
            if(inputs.data.horizontals.containsKey(v)){
                top = inputs.data.horizontals.get(v);
            }
        }

        if(node.containsInputValue("bottom")){
            var v = node.getInputValue("bottom");
            if(inputs.data.horizontals.containsKey(v)){
                bottom = inputs.data.horizontals.get(v);
            }
        }

        if(node.containsInputValue("left")){
            var v = node.getInputValue("left");
            if(inputs.data.verticals.containsKey(v)){
                left = inputs.data.verticals.get(v);
            }
        }

        if(node.containsInputValue("right")){
            var v = node.getInputValue("right");
            if(inputs.data.verticals.containsKey(v)){
                right = inputs.data.verticals.get(v);
            }
        }

        outputs.put("width", Math.abs(left - right));
        outputs.put("height", Math.abs(top - bottom));
        return outputs;
    }
}
