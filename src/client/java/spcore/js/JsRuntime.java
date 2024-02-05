package spcore.js;

import imgui.ImGui;
import net.fabricmc.loader.impl.game.LibClassifier;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import spcore.js.functions.JsFunc;

import javax.script.*;

public class JsRuntime {

    private final Context context;
    public JsRuntime(){

        context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .build();


        setBindings();
    }

    private void setBindings(){
        context.getBindings("js").putMember("imGui", ImGui.class);
    }

    public Value eval(String js) throws ScriptException {
        Source source = Source.create("js", js);
        return context.eval(source);
    }

    public Value eval(JsFunc func) throws ScriptException {
        return eval(func.get());
    }




}
