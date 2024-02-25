package spcore.fabric.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import spcore.engine.ViewContext;
import spcore.js.JsRuntime;
import spcore.view.render.Renderable;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.List;

public class WrapperedScreen extends Screen {

    protected ViewContext viewContext;
    private final HashMap<String, Integer> wrappers = new HashMap<>();
    private HashMap<String, List<Renderable>> linksWrappers;
    protected WrapperedScreen(ViewContext viewContext, Text title) {
        super(title);
        this.viewContext = viewContext;
    }

    public void initWrappers(int controllerId, HashMap<String, List<Renderable>> wrappers) throws ScriptException {
        this.linksWrappers = wrappers;
        for (var wrap: wrappers.entrySet()
             ) {

            var index = viewContext.getWrapperIndex(controllerId, wrap.getKey());
            this.wrappers.put(wrap.getKey(), index);
        }
    }

    public void renderWrappers(int controllerId) throws ScriptException {
        for (var wrap: linksWrappers.entrySet()
             ) {
            for (var c: wrap.getValue()
                 ) {
                viewContext.startWrapper(controllerId, wrappers.get(wrap.getKey()), c);
            }
        }
    }

    public ViewContext getViewContext(){
        return viewContext;
    }



}
