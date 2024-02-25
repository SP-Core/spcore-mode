package spcore.imgui.nodes.types.view;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.components.BorderableComponent;

import java.util.HashMap;

public class BorderViewNodeType extends ViewNodeType {


    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = super.internalCreateInfo(nt);

        node.addInput(new PinInfo("border-top", PinType.Int));
        node.addInput(new PinInfo("border-bottom", PinType.Int));
        return node;
    }

    @Override
    public ViewComponent createComponent() {
        return new BorderableComponent();
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        var outputs = super.internalProcess(node, inputs);

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

