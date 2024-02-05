package spcore.engine;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import spcore.fabric.screens.AppScreen;
import spcore.imgui.ImGuiImpl;
import spcore.js.JsRuntime;
import spcore.js.functions.GetViewFunc;

import javax.script.ScriptException;

public class AppEngine {

    private final Window window;

    private static AppEngine currentEngine;
    public AppEngine(Window window) {
        if(currentEngine != null){
            try {
                throw new Exception("Повторная инициализация AppEngine");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.window = window;
        currentEngine = this;
    }
    public static AppEngine getInstance(){
        return currentEngine;
    }


    public void init(){
        ImGuiImpl.create(window.getHandle());
    }

    public void runApp(String js) throws ScriptException {
        var runtime = new JsRuntime();
        runtime.eval("module = {}");
        runtime.eval(new GetViewFunc());
        var screan = new AppScreen(runtime);
        runtime.eval(js);
        screan.MoveToView("/");
        MinecraftClient.getInstance().setScreen(screan);
    }
}
