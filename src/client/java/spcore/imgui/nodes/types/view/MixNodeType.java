package spcore.imgui.nodes.types.view;

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
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MixNodeType extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);

        node.addInput(new PinInfo("parent", PinType.Component));
        node.addInput(new PinInfo("components", PinType.Components));

        node.addOutput(new PinInfo("output", PinType.Component));
        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        var parent = (Renderable)inputs.process("parent");
        if(parent == null)
        {
            outputs.put("output", new ViewComponent());
            return outputs;
        }

        var components = (List<Renderable>)inputs.process("components");
        if(components == null){
            outputs.put("output", parent);
            return outputs;
        }

        for (var child: components
             ) {
            parent.addChild(child);
        }

        outputs.put("output", parent);
        return outputs;
    }
}
