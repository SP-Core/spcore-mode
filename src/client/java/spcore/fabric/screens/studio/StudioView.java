package spcore.fabric.screens.studio;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import spcore.GlobalContext;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.render.RenderEngine;

import java.io.IOException;
import java.util.HashMap;

public class StudioView extends Screen {
    public final NodeContext nodeContext;
    private boolean show = true;
    public StudioView(String viewName) {
        super(Text.of(viewName));
        try {
            this.nodeContext = new NodeContext(viewName, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void init(){
        super.init();
        nodeContext.init();

        //ProcessService.cache = new HashMap<>();
        var view =  nodeContext.startProcess(show);
        RenderEngine.Run(view, this::addDrawable);
    }

    public void restart(){
        super.clearAndInit();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(this.show){
            ImGuiImpl.draw(io -> {
                nodeContext.render.render();
            });
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 258 && scanCode == 15 && modifiers == 0){
            this.show = !show;
            this.clearAndInit();
        }
//        GlobalContext.LOGGER.info(" " + keyCode + " " + scanCode + " " + modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
