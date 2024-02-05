package spcore.fabric.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import spcore.GlobalContext;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.NodeEditorRender;
import spcore.js.JsRuntime;

import javax.script.ScriptException;

@Environment(value= EnvType.CLIENT)

public class AppScreen extends Screen {

    private final JsRuntime jsRuntime;

    private String appUrl;
    private Object appUrlIndex;
    private final MinecraftClient mc;
    public AppScreen(JsRuntime jsRuntime) {
        super(NarratorManager.EMPTY);
        this.jsRuntime = jsRuntime;
        mc = MinecraftClient.getInstance();
    }

    public void MoveToView(String url){
        appUrl = url;
        try {
            appUrlIndex = jsRuntime.eval("getView(module.exports.core.routes, \"" + url +"\")");
            if(appUrlIndex.toString().equals("null")){
                GlobalContext.LOGGER.info("View not found");
                mc.setScreen((Screen) null);
                return;
            }

        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(appUrlIndex == null){
            mc.setScreen((Screen) null);
            return;
        }

        ImGuiImpl.draw(io -> {
            try {
                jsRuntime.eval("module.exports.core.routes["+ appUrlIndex +"].handler.render()");
            } catch (ScriptException e) {
                GlobalContext.LOGGER.info(e.getMessage());
                mc.setScreen((Screen) null);
                return;
            }
        });

    }
}
