package spcore.view.render;

import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

public class RenderEngine {
    public static void Run(Renderable element, Consumer<Renderable> consumer){
        var ctx = new RenderContext();
        element.configure(null, ctx);
        element.refreshPositions();
        element.cache();
        element.forEachAllChild(consumer);
    }
}
