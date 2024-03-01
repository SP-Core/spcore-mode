package spcore.imgui.nodes.types.variables;

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

public class ScreenNodeType extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);

        node.addOutput(new PinInfo("width", PinType.Float));
        node.addOutput(new PinInfo("height", PinType.Float));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        var outputs = new HashMap<String, Object>();
        outputs.put("width", inputs.render.context.scope.width);
        outputs.put("height", inputs.render.context.scope.height);

        return outputs;
    }
}
