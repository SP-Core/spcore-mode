package spcore.imgui.nodes.types;

import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;

public class StylesNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Styles";
    }

    @Override
    public Node create(NodeContext context) {
        return null;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        return null;
    }
}
