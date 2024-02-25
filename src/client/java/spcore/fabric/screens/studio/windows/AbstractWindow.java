package spcore.fabric.screens.studio.windows;

import imgui.type.ImBoolean;
import spcore.fabric.screens.studio.StudioView;

import java.util.HashMap;

public abstract class AbstractWindow {

    protected final HashMap<String, Object> data = new HashMap<>();
    private StudioView screen;
    protected ImBoolean isShow = new ImBoolean(false);
    public void setScreen(StudioView input){
        this.screen = input;
    }

    public StudioView getScreen(){
        return this.screen;
    }
    public void setData(String key, Object value){
        this.data.put(key, value);
    }
    public abstract String getName();

    public abstract String getId();
    public boolean toggle(){
        isShow = new ImBoolean(!isShow.get());
        return isShow.get();
    }

    public boolean isShow(){
        return isShow.get();
    }
    public abstract void render();
    public abstract void init();
}
