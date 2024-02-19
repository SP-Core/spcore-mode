package spcore.imgui.nodes.types;

import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.PinId;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractNodeType {
    public abstract String getName();


    public abstract Node create(NodeContext context);

    public abstract HashMap<String, Object> process
            (Node node, ProcessService inputs);
}
