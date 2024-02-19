package spcore.imgui.nodes.types;

import imgui.ImColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;

public class TextNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "text";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "value",
                        PinType.String));

        node.outputs.add(
                new Pin(context.nextId(),
                        "value",
                        PinType.String));

        return node;

    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
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
