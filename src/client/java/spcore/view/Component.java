package spcore.view;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import spcore.view.render.Renderable;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface Component {
    void setX(String x);

    void setY(String y);

    float getX();

    float getY();

    float getWidth();

    float getHeight();

//    default ScreenRect getNavigationFocus() {
//        return new ScreenRect((int)this.getX(), (int)this.getY(), this.getWidth(), this.getHeight());
//    }


    void forEachChild(Consumer<Renderable> consumer);

}
