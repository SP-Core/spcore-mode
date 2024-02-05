package spcore.fabric.screens;

import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.flag.ImGuiCond;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import spcore.GlobalContext;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.Graph;
import spcore.imgui.nodes.NodeEditorRender;

import java.awt.*;
import java.net.URI;

@Environment(value= EnvType.CLIENT)
public class NodeEditorScreen extends Screen {

    public final Graph graph;
    public NodeEditorScreen() {
        super(NarratorManager.EMPTY);
        graph = new Graph();

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ImGuiImpl.draw(io -> {
            NodeEditorRender.Render(io, graph);
        });
    }
}
