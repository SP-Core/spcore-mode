package spcore.appapi.commands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SelectionManager;
import spcore.fabric.screens.ImGuiHelloScreen;
import spcore.fabric.screens.NodeEditorScreen;

import java.util.HashMap;

public class ImGuiHelloCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        var mc = MinecraftClient.getInstance();
        mc.setScreen(new ImGuiHelloScreen());
        return false;
    }

    @Override
    public String GetDescription() {
        return "ImGui demo";
    }
}
