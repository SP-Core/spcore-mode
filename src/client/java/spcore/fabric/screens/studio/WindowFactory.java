package spcore.fabric.screens.studio;

import spcore.fabric.screens.studio.windows.AbstractWindow;

public class WindowFactory<T extends AbstractWindow> {
    public T create(Class<T> clazz, StudioView screen){
        T window = null;
        try {
            window = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        window.setScreen(screen);
        return window;
    }
}
