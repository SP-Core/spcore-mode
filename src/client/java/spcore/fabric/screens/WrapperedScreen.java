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
            if(index == -1){
                continue;
            }
            this.wrappers.put(wrap.getKey(), index);
        }
    }

    public void renderWrappers(RenderInfo renderInfo, int controllerId) throws ScriptException {
        for (var wrap: linksWrappers.entrySet()
             ) {
            if(!wrappers.containsKey(wrap.getKey())){
                continue;
            }
            for (var c: wrap.getValue()
                 ) {
                viewContext.jsRuntime.putMember("Render", renderInfo);
                viewContext.startWrapper(controllerId, wrappers.get(wrap.getKey()), c);
            }
        }
    }

    public ViewContext getViewContext(){
        return viewContext;
    }

    public static class RenderInfo{
        private final float mouseX;
        private final float mouseY;
        public RenderInfo(float mouseX, float mouseY){
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }
        public float mousePosX(){
            return mouseX;
        }

        public float mousePosY(){
            return mouseY;
        }
    }


}
