package spcore.view.components;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import spcore.view.ViewComponent;

import java.awt.geom.Point2D;

public class BorderableComponent extends ViewComponent {

    public BorderableComponent(){
        super();
        super.style = new BorderableStyles();
    }

    @Override
    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {
        super.internalRender(context, mouseX, mouseY, delta);
        float x1 = getX(); float y1 = getY(); float x2 = getX2(); float y2 = getY2();

        float top;
        float left;
        float right;
        float bottom;
        if(y1 > y2){
            top = y2;
            bottom = y1;
        }
        else{
            top = y1;
            bottom = y2;
        }

        if(x1 > x2){
            right = x2 + getWidth();
            left = x1 - getWidth();
        }
        else{
            right = x1 + getWidth();
            left = x2- getWidth();
        }


        var top_border = this.style.get(BorderableStyles.BORDER_TOP, int.class);

        Point2D.Float[] topBorder_points =
                {
                        new Point2D.Float(right, top),
                        new Point2D.Float(right, top - top_border),
                        new Point2D.Float(left, top - top_border),
                        new Point2D.Float(left - top_border, top)
                };
        renderSquare(context, topBorder_points, style.get(ViewStyles.BACKGROUND, int.class));

        var bottom_border = this.style.get(BorderableStyles.BORDER_BOTTOM, int.class);

        Point2D.Float[] bottomBorder_points =
                {
                        new Point2D.Float(right, bottom + bottom_border),
                        new Point2D.Float(right + top_border, bottom),
                        new Point2D.Float(left, bottom),
                        new Point2D.Float(left, bottom + bottom_border),
                };
        renderSquare(context, bottomBorder_points, style.get(ViewStyles.BACKGROUND, int.class));


    }

    public static class BorderableStyles extends ViewStyles{
        public static final String BORDER_TOP = "border-top";
        public static final String BORDER_LEFT = "border-left";
        public static final String BORDER_RIGHT = "border-right";
        public static final String BORDER_BOTTOM = "border-bottom";

        public BorderableStyles(){
            addHandler(BORDER_TOP, "5", int.class, Integer::parseInt);
            addHandler(BORDER_BOTTOM, "5", int.class, Integer::parseInt);
            addHandler(BORDER_LEFT, "5", int.class, Integer::parseInt);
            addHandler(BORDER_RIGHT, "5", int.class, Integer::parseInt);
        }
    }
}
