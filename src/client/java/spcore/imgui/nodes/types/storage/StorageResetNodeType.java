package spcore.imgui.nodes.types.storage;

import com.mojang.datafixers.View;
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
import spcore.view.ViewComponent;

import java.util.HashMap;

public class StorageResetNodeType extends AbstractNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("component", PinType.Component));
        node.addOutput(new PinInfo("component", PinType.Component));

        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        inputs.data.statics.clear();
        HashMap<String, Object> outputs = new HashMap<>();
        if(inputs.contains("component")){
            outputs.put("component", inputs.process("component"));
        }
        else{
            outputs.put("component", new ViewComponent());
        }

        return outputs;
    }
}
