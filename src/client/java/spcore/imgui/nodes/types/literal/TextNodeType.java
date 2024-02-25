package spcore.imgui.nodes.types.literal;

import imgui.ImColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
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

public class TextNodeType extends AbstractNodeType {
    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("value", PinType.String));
        node.addOutput(new PinInfo("value", PinType.String));

        return node;
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        if(inputs.contains("value")){
            outputs.put("value", inputs.process("value"));
        }
        else if(node.containsInputValue("value")){
            outputs.put("value", node.getInputValue("value"));
        }
        else{
            outputs.put("value", "");
        }

        return outputs;
    }
}
