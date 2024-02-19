package spcore.view.tooltip;

import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import spcore.view.Component;

public class CoreFocusedTooltipPositioner implements TooltipPositioner {
    private final Component component;

    public CoreFocusedTooltipPositioner(Component component) {
        this.component = component;
    }

    @Override
    public Vector2ic getPosition(int screenWidth, int screenHeight, int x, int y, int width, int height) {
        Vector2i vector2i = new Vector2i();
        vector2i.x = component.getX() + 3;
        vector2i.y = component.getY() + component.getHeight() + 3 + 1;
        if (vector2i.y + height + 3 > screenHeight) {
            vector2i.y = component.getY() - height - 3 - 1;
        }

        if (vector2i.x + width > screenWidth) {
            vector2i.x = Math.max(component.getX() + component.getWidth() - width - 3, 4);
        }

        return vector2i;
    }
}
