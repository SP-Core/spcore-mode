package spcore.imgui.nodes.types.guides;

import imgui.ImColor;
import org.joml.Vector2f;
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

public class GuideLink extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("vertical", PinType.String));
        node.addInput(new PinInfo("horizontal", PinType.String));
        node.addOutput(new PinInfo("vector", PinType.Vector2));

        return node;
    }


    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var vector = new Vector2f();
        outputs.put("vector", vector);

        if(inputs.contains("vertical")){
            var v = (String) inputs.process("vertical");
            if(inputs.data.verticals.containsKey(v)){
                vector.x = inputs.data.verticals.get(v);
            }
        }
        else if(node.containsInputValue("vertical")){
            var v = node.getInputValue("vertical");
            if(inputs.data.verticals.containsKey(v)){
                vector.x = inputs.data.verticals.get(v);
            }

        }

        if(inputs.contains("horizontal")){
            var v = (String) inputs.process("horizontal");
            if(inputs.data.horizontals.containsKey(v)){
                vector.y = inputs.data.horizontals.get(v);
            }
        }
        else if(node.containsInputValue("horizontal")){
            var v = node.getInputValue("horizontal");
            if(inputs.data.horizontals.containsKey(v)){
                vector.y = inputs.data.horizontals.get(v);
            }

        }

        return outputs;
    }


}
