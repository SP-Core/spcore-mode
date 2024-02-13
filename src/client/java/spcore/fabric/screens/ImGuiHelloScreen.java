package spcore.fabric.screens;

import imgui.ImGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
//import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.Graph;

@Environment(value= EnvType.CLIENT)
public class ImGuiHelloScreen extends Screen {
    public ImGuiHelloScreen() {
        super(NarratorManager.EMPTY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        ImGuiImpl.draw(io -> {
//            ImGui.showDemoWindow();
//        });
    }
}
