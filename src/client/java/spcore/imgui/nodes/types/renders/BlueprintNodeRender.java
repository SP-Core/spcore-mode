package spcore.imgui.nodes.types.renders;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.NodeRender;
import spcore.imgui.nodes.inputs.AbstractValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.utils.BlueprintNodeBuilder;
import spcore.view.render.RenderContext;

public class BlueprintNodeRender extends AbstractNodeRender {

    private BlueprintNodeBuilder builder ;
    private NodeRender render;
    @Override
    public void init(NodeRender context) {
        render = context;
        builder = new BlueprintNodeBuilder(context.context.textureId, context.context.width, context.context.height);
    }

    @Override
    public void render(Node node) {
        builder.begin(node.id);
        builder.header(node.color);
        ImGui.textUnformatted(node.name);
        ImGui.dummy(0, 28);



        builder.endHeader();

        for (var input: node.inputs
        ) {
            var alpha = ImGui.getStyle().getAlpha();

            if (render.newLinkPin != null && !render.CanCreateLink(render.newLinkPin, input) && input != render.newLinkPin) {
                alpha = alpha * (48.0f / 255.0f);
            }
            ImGui.pushID(node.id.toString());
            builder.Input(input.id);
            ImGui.pushStyleVar(ImGuiStyleVar.Alpha, alpha);
            render.DrawPinIcon(input, render.IsPinLinked(input.id), (int)(alpha * 255));
            ImGui.sameLine(0);

            ImGui.textUnformatted(input.name);

            if(render.context.links.stream().noneMatch(p -> p.startPinId.equals(input.id))){
                ImGui.sameLine(0);
                if(AbstractValueInput.Inputs.containsKey(input.type)){
                    var b = AbstractValueInput.Inputs.get(input.type);

                    b.render(node, input);
                }
            }

            ImGui.popStyleVar();
            builder.EndInput();
            ImGui.popID();
        }

        for (var output: node.outputs
        ) {

            var alpha = ImGui.getStyle().getAlpha();
            if (render.newLinkPin != null && !render.CanCreateLink(render.newLinkPin, output) && output != render.newLinkPin) {
                alpha = alpha * (48.0f / 255.0f);
            }

            ImGui.pushStyleVar(ImGuiStyleVar.Alpha, alpha);
            ImGui.newLine();
            float i = 0.0f;
            if (!output.name.isEmpty()) {
                i = ImGui.calcTextSize(output.name, true).x;
            }
            ImGui.sameLine(0);

            builder.Output(output.id);

            if (!output.name.isEmpty()) {
                ImGui.sameLine(0, 300 - i);
                ImGui.textUnformatted(output.name);
            }
            ImGui.sameLine(0);
            render.DrawPinIcon(output, render.IsPinLinked(output.id), (int)(alpha * 255));
            ImGui.popStyleVar();
            builder.EndOutput();

        }

        builder.End();
    }
}
