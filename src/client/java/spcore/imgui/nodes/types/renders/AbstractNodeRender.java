package spcore.imgui.nodes.types.renders;

import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.NodeRender;
import spcore.imgui.nodes.models.Node;
import spcore.view.render.RenderContext;

public abstract class AbstractNodeRender {
    public abstract void init(NodeRender context);
    public abstract void render(Node node);
}
