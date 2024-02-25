package spcore.imgui.nodes.types.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.joml.Vector4f;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.components.TextComponent;

import java.util.HashMap;

public class TextViewNodeType extends ViewNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = super.internalCreateInfo(nt);

        node.addInput(new PinInfo("text", PinType.String));
        node.addInput(new PinInfo("scale", PinType.Float));
        node.addInput(new PinInfo("color", PinType.Vector4));
        return node;
    }

    @Override
    public ViewComponent createComponent() {
        return new TextComponent("text", MinecraftClient.getInstance().textRenderer);
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        var outputs = super.internalProcess(node, inputs);

        var component = (TextComponent) outputs.get("component");



        Text tx = Text.of("txt");
        if (inputs.contains("text")) {

            tx = net.minecraft.text
                    .Text.of(inputs.process("text").toString());


        } else if (node.containsInputValue("text")) {

            tx = net.minecraft.text.Text.of(node.getInputValue("text"));
        }

        if (node.containsInputValue("scale")) {

            var f = Float.parseFloat(node.getInputValue("scale"));
            component.setScale(f);
        }

        component.setMessage(tx);
        if (inputs.contains("color")) {
            var vector4 = (Vector4f) inputs.process("color");

            int red = (int) (vector4.x * 255);
            int green = (int) (vector4.y * 255);
            int blue = (int) (vector4.z * 255);
            int alpha = (int) (vector4.w * 255);

            int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;

            String hexString = String.format("#%08X", argb);

            component.getStyle().styles
                    .put(TextComponent.TextStyles.COLOR, hexString);
        } else if (node.containsInputValue("color")) {
            var vector = Vector4ValueInput.parse(node.getInputValue("color"));
            int red = (int) (vector.x * 255);
            int green = (int) (vector.y * 255);
            int blue = (int) (vector.z * 255);
            int alpha = (int) (vector.w * 255);

            int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;

            String hexString = String.format("#%08X", argb);

            component.getStyle().styles
                    .put(TextComponent.TextStyles.COLOR, hexString);
        }


        return outputs;
    }
}
