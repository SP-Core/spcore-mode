package spcore.engine;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.NarratorManager;
import spcore.GlobalContext;
import spcore.appapi.models.SpCoreInfo;
import spcore.engine.models.CommandMapItem;
import spcore.fabric.screens.NotFoundViewScreen;
import spcore.fabric.screens.TerminalScreen;
import spcore.fabric.screens.views.CommandMapScreen;
import spcore.fabric.screens.views.ScriptExceptionScreen;
import spcore.js.JsRuntime;

import javax.script.ScriptException;

public class ViewContext {
    private String appUrl;
    private Object appUrlIndex;
    private final JsRuntime jsRuntime;
    private final SpCoreInfo app;
    private final MinecraftClient mc;

    public ViewContext(JsRuntime jsRuntime, SpCoreInfo app) {
        this.jsRuntime = jsRuntime;
        this.app = app;
        mc = MinecraftClient.getInstance();
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

        viewNotFound();
    }

    private void viewNotFound(){
        GlobalContext.LOGGER.info("View not found");
        mc.setScreen(new NotFoundViewScreen(app));
    }

    private void viewCommandMap(CommandMapItem[] mapItems){
        mc.setScreen(new CommandMapScreen(TerminalScreen.getCurrent(), mapItems));
    }

    private void viewException(Exception e){
        GlobalContext.LOGGER.error(e.getMessage());
        mc.setScreen(new ScriptExceptionScreen(app, e));
    }
    public boolean viewIsNotFound(){
        return appUrlIndex.toString().equals("null");
    }

}
