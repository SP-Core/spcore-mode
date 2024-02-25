package spcore.js;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.fabricmc.loader.impl.game.LibClassifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BoneMealItem;
import net.minecraft.util.ActionResult;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import spcore.GlobalContext;
import spcore.api.AuthContext;
import spcore.engine.TimeApi;
import spcore.js.functions.JsFunc;

import javax.script.*;
import java.util.Objects;

public class JsRuntime {

    private final Context context;
    public JsRuntime(String hostAccess){

        HostAccess host = null;
        if(hostAccess != null && hostAccess.equals("ALL")){
            host = HostAccessBuilder.buildALL();
        }
        else{
            host = HostAccessBuilder.buildISOLATED();
        }

        context = Context.newBuilder("js")
                .allowHostAccess(host)
                .build();


        setBindings();
    }

    private void setBindings(){
        var bindings = context.getBindings("js");

        bindings.putMember("MC", MinecraftClient.getInstance());
//        context.getBindings("js").putMember("imGuiStatic", ImGui.class);
        bindings.putMember("globalStatic", GlobalContext.class);
        bindings.putMember("native", new nativeService());
        bindings.putMember("authStatic", AuthContext.class);
        bindings.putMember("ObjectsStatic", Objects.class);
        bindings.putMember("MathStatic", Math.class);
        bindings.putMember("HttpHelperStatic", HttpHelper.class);
        bindings.putMember("timeStatic", TimeApi.class);

        for (var type: AssemblyHelper.allTypes
             ) {
            bindings.putMember(
                    AssemblyHelper.ResolveName(type.getName(),
                    type.getSimpleName()),
                    type);
        }

        for (var type: AssemblyHelper.allEnums
        ) {
            bindings.putMember(
                    AssemblyHelper.ResolveName(type.getName(),
                            type.getSimpleName()) + "Static",
                    type);
        }

//        context.getBindings("js").putMember("BoneMealItem", BoneMealItem.class);
//        context.getBindings("js").putMember("ActionResultStatic", ActionResult.class);
    }

    public Value eval(String js, boolean verified) throws ScriptException {

        if(!verified){
            js = "try{\n" +
                    js +
                    "}catch(e){\n" +
                    "native.exception(e.toString())\n" +
                    "}";
        }

        Source source = Source.create("js", js);
        Value value;
        try{
            value = context.eval(source);

        }
        catch (Exception e){
            throw new ScriptException(e);
        }
        catch (Error error){
            throw new ScriptException(error.getMessage());
        }
        Exception e = null;
        try{
            e = value.as(Exception.class);
            if(e == null){
                throw new Exception();
            }
        }
        catch (Exception je){
            return value;
        }

        throw new ScriptException(e);

    }

    public void putMember(String key, Object value){
        context.getBindings("js").putMember(key, value);
    }

    public Value eval(JsFunc func) throws ScriptException {
        return eval(func.get(), true);
    }

    public Value eval(String js) throws ScriptException {
        return eval(js, false);
    }

    public Value evalVerified(String js) throws ScriptException {
        return eval(js, true);
    }



}
