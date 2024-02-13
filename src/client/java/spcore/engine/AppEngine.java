package spcore.engine;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import spcore.GlobalContext;
import spcore.appapi.helpers.PathHelper;
import spcore.appapi.models.SpCoreInfo;
import spcore.fabric.screens.AppScreen;
//import spcore.imgui.ImGuiImpl;
import spcore.js.AssemblyHelper;
import spcore.js.JsRuntime;
import spcore.js.functions.GetCommandFunc;
import spcore.js.functions.GetViewFunc;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        //ImGuiImpl.create(window.getHandle());
    }

    public void runApp(JsRuntime runtime, SpCoreInfo app) throws ScriptException {

        var context = new ViewContext(runtime, app);
        context.MoveToView("/");
    }


    public String getSource(SpCoreInfo core){
        try {
            return Files.readString(Path.of(PathHelper.combine(core.absolute, core.exe)));
        } catch (IOException e) {
            return null;
        }

    }

    public JsRuntime createRuntime(String js, String hostAccess) throws ScriptException {
        var runtime = new JsRuntime(hostAccess);
        runtime.evalVerified("module = {}");
//        runtime.evalVerified("const ImGui = imGuiStatic.static");
        runtime.evalVerified("const globalContext = globalStatic.static");
        runtime.evalVerified("const auth = authStatic.static");
        runtime.evalVerified("const Objects = ObjectsStatic.static");
        runtime.evalVerified("const Math = MathStatic.static");
        runtime.evalVerified("const HttpHelper = HttpHelperStatic.static");

//        runtime.evalVerified("const ActionResult = ActionResultStatic.static");

        for (var type: AssemblyHelper.allTypes
        ) {
            var rName = AssemblyHelper.ResolveName(type.getName(),
                    type.getSimpleName());
            try{
                runtime.evalVerified("const Static_" + rName + " = " + rName + ".static");
            }
            catch (ScriptException e){
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }

        for (var type: AssemblyHelper.allEnums
        ) {
            var rName = AssemblyHelper.ResolveName(type.getName(),
                    type.getSimpleName());

            try{
                runtime.evalVerified("const " + rName + " = " + rName + "Static" );
            }
            catch (ScriptException e){
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }

        runtime.eval(new GetViewFunc());
        runtime.eval(new GetCommandFunc());
        runtime.eval(js);
        return runtime;
    }
}
