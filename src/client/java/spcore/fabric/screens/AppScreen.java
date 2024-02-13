package spcore.fabric.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import spcore.GlobalContext;
//import spcore.imgui.nodes.NodeEditorRender;
import spcore.appapi.models.SpCoreInfo;
import spcore.js.JsRuntime;

import javax.script.ScriptException;
import java.util.Objects;
import java.util.function.Supplier;

@Environment(value= EnvType.CLIENT)

public class AppScreen extends Screen {

    private final JsRuntime jsRuntime;
    private final SpCoreInfo app;
    private String appUrl;
    private Object appUrlIndex;
    private final MinecraftClient mc;
    public AppScreen(JsRuntime jsRuntime, SpCoreInfo app) {
        super(NarratorManager.EMPTY);
        this.jsRuntime = jsRuntime;
        this.app = app;
        mc = MinecraftClient.getInstance();
    }


    public boolean viewIsNotFound(){
        return appUrlIndex.toString().equals("null");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(viewIsNotFound()){
            mc.setScreen(new NotFoundViewScreen(app));
        }
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }


}
