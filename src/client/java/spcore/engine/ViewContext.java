package spcore.engine;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import spcore.GlobalContext;
import spcore.appapi.helpers.PathHelper;
import spcore.appapi.models.SpCoreInfo;
import spcore.engine.models.CommandMapItem;
import spcore.engine.models.ViewController;
import spcore.engine.models.ViewsInfo;
import spcore.fabric.screens.NotFoundViewScreen;
import spcore.fabric.screens.TerminalScreen;
import spcore.fabric.screens.WrapperedScreen;
import spcore.fabric.screens.studio.InGameHubHandler;
import spcore.fabric.screens.studio.InGameHudCoreContext;
import spcore.fabric.screens.studio.StudioView;
import spcore.fabric.screens.views.CommandMapScreen;
import spcore.fabric.screens.views.ScriptExceptionScreen;
import spcore.imgui.nodes.NodeContext;
import spcore.js.JsRuntime;
import spcore.view.render.Renderable;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ViewContext {
    private String appUrl;
    public Object appUrlIndex;
    public final JsRuntime jsRuntime;
    private final SpCoreInfo app;
    private final MinecraftClient mc;

    public final List<ViewController> viewControllers;
    public ViewContext(JsRuntime jsRuntime, SpCoreInfo app) {
        this.jsRuntime = jsRuntime;
        this.app = app;
        mc = MinecraftClient.getInstance();
        try {
            viewControllers = Arrays.stream(jsRuntime
                    .eval("module.exports.core.controllers")
                    .as(ViewController[].class))
                    .toList();

            for(var i = 0; i < viewControllers.size(); i++){
                var v = viewControllers.get(i);
                v.index = i;
            }
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void MoveToView(String url){
        appUrl = url;
        try {
            appUrlIndex = jsRuntime.eval("getView(module.exports.core.routes, \"" + url +"\")");
            if(viewIsNotFound()){
                viewNotFound();
            }
            else{
                var viewType = jsRuntime
                        .eval("module.exports.core.routes["+ appUrlIndex +"].handler.type")
                        .asString();

                viewHandler(viewType);
            }

        } catch (Exception e) {
            viewException(e);
        }
    }

    private void viewHandler(String view) throws ScriptException {
        if ("command-map".equals(view)) {
            var commands = jsRuntime
                    .eval("module.exports.core.routes["+ appUrlIndex +"].handler.commandMap")
                    .as(CommandMapItem[].class);
            viewCommandMap(commands);
            return;
        }

        if("view".equals(view)){
            var viewControllerName = jsRuntime
                    .eval("module.exports.core.routes["+ appUrlIndex +"].handler.viewControllerName")
                    .as(String.class);

            var viewController = viewControllers.stream()
                    .filter(p -> p.name.equals(viewControllerName))
                    .findFirst()
                    .get();

            view(viewController);
            return;
        }


        viewNotFound();
    }

    private void viewNotFound(){
        GlobalContext.LOGGER.info("View not found");
        mc.setScreen(new NotFoundViewScreen(app));
    }

    private void viewCommandMap(CommandMapItem[] mapItems){
        mc.setScreen(new CommandMapScreen(TerminalScreen.getCurrent(), mapItems));
    }

    private void view(ViewController controller) throws ScriptException {

        StudioView screen = null;
        if(app.dev != null && app.dev.views != null){
            var viewsPath = app.dev.views + "/views.json";
            var file = new File(PathHelper.combine(app.absolute, viewsPath));
            if(Files.exists(file.toPath())){
                screen = new StudioView(this, app, controller, file, false);
                mc.setScreen(screen);
                return;
            }
        }

        screen = new StudioView(this, app, controller, null, true);
        mc.setScreen(screen);


    }

    private void viewException(Exception e){
        GlobalContext.LOGGER.error(e.getMessage());
        mc.setScreen(new ScriptExceptionScreen(app, e));
    }
    public boolean viewIsNotFound(){
        return appUrlIndex.toString().equals("null");
    }

    public Integer getWrapperIndex(int controllerId, String id) throws ScriptException {
        var wrapperId = jsRuntime
                .eval("getWrapper(module.exports.core.controllers["+ controllerId +"]" +
                        ".wrappers, \"" + id +"\")");

        if(wrapperId.isNull()){
            return -1;
        }
        return wrapperId.asInt();
    }

    public void startWrapper(int controllerId, int index, Renderable renderable) throws ScriptException {
        jsRuntime.putMember("back_renderable", renderable);

        jsRuntime.eval("module.exports.core.controllers["+ controllerId +"].wrappers["+ index +"].wrapper(back_renderable)");
    }

}
