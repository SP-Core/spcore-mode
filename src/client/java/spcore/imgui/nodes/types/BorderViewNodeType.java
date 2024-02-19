package spcore.imgui.nodes.types;

import net.minecraft.client.MinecraftClient;
import org.joml.Vector4f;
import org.w3c.dom.Text;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.components.BorderableComponent;
import spcore.view.components.TextComponent;

import java.util.HashMap;

public class BorderViewNodeType extends ViewNodeType {
    @Override
    public String getName() {
        return "Border view";
    }

    @Override
    public Node create(NodeContext context) {
        var node = super.create(context);
        node.inputs.add(new Pin(context.nextId(),
                "border-top",
                PinType.Int));

        node.inputs.add(new Pin(context.nextId(),
                "border-bottom",
                PinType.Int));

        return node;
    }

    @Override
    public ViewComponent createComponent() {
        return new BorderableComponent();
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        var outputs = super.process(node, inputs);

        var component = (ViewComponent) outputs.get("component");

        if (inputs.contains("border-top")) {
            component.getStyle().styles
                    .put(
                            BorderableComponent.BorderableStyles.BORDER_TOP,
                            inputs.process("border-top").toString());
        } else if (node.containsInputValue("border-top")) {
            component.getStyle().styles
                    .put(
                            BorderableComponent.BorderableStyles.BORDER_TOP,
                            node.getInputValue("border-top"));
        }

        if (inputs.contains("border-bottom")) {
            component.getStyle().styles
                    .put(
                            BorderableComponent.BorderableStyles.BORDER_BOTTOM,
                            inputs.process("border-bottom").toString());
        } else if (node.containsInputValue("border-bottom")) {
            component.getStyle().styles
                    .put(
                            BorderableComponent.BorderableStyles.BORDER_BOTTOM,
                            node.getInputValue("border-bottom"));
        }

        return outputs;
    }
}

