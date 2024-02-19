package spcore.view;

import net.minecraft.client.gui.*;
import net.minecraft.client.render.*;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import okio.Options;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import spcore.view.render.RenderContext;
import spcore.view.render.Renderable;

import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Float.NaN;

public class ViewComponent extends Renderable implements Drawable {
    public ViewComponent(){
        super(new Vector2i(0, 0));
        super.style = new ViewStyles();
    }

    @Override
    protected void internalConfigure(RenderContext render) {

    }
    @Override
    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {


        int x1 = getX(); int y1 = getY(); int x2 = getX2(); int y2 = getY2();
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }

        Point2D.Float[] square =
                {
                        new Point2D.Float(x1, y1),
                        new Point2D.Float(x1, y2),
                        new Point2D.Float(x2, y2),
                        new Point2D.Float(x2, y1)
                };

        var rotate = getRotate();
        if(rotate != null){
            square = rotateSquare(square, rotate);
            square = locked(square);
        }

        renderSquare(context, square, style.get(ViewStyles.BACKGROUND, int.class));
    }



    public Point2D.Float[] locked(Point2D.Float[] points){
        if(style.get(ViewStyles.ROTATE_LOCK_Y, Boolean.class)){
            points = new Point2D.Float[]{
                    new Point2D.Float(points[0].x, getY()),
                    new Point2D.Float(points[1].x, getY2()),
                    new Point2D.Float(points[2].x, getY2()),
                    new Point2D.Float(points[3].x, getY()),
            };
        }

        if(style.get(ViewStyles.ROTATE_LOCK_X, Boolean.class)){
            points = new Point2D.Float[]{
                    new Point2D.Float(getX(), points[0].y),
                    new Point2D.Float(getX(), points[1].y),
                    new Point2D.Float(getX2(), points[2].y),
                    new Point2D.Float(getX2(), points[3].y),
            };
        }

        return points;
    }

    public static void renderSquare(DrawContext ctx, Point2D.Float[] points, int color){
        Matrix4f matrix4f = ctx.getMatrices().peek().getPositionMatrix();
        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        BufferBuilder vertexConsumer = (BufferBuilder)ctx.getVertexConsumers().getBuffer(RenderLayer.getGui());
        for (var point: points
             ) {
            vertexConsumer.vertex(matrix4f, (float)point.x, (float)point.y, 0).color(g, h, j, f).next();
        }
        ctx.draw();
    }


    public static Point2D.Float rotatePoint(Point2D.Float point, float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float newX = (float) (point.getX() * Math.cos(radians) - point.getY() * Math.sin(radians));
        float newY = (float) (point.getX() * Math.sin(radians) + point.getY() * Math.cos(radians));
        return new Point2D.Float(newX, newY);
    }

    public static Point2D.Float[] rotateSquare(Point2D.Float[] square, float degrees) {
        // Находим центр квадрата
        float centerX = 0, centerY = 0;
        for (Point2D.Float point : square) {
            centerX += point.x;
            centerY += point.y;
        }
        centerX /= 4;
        centerY /= 4;

        Point2D.Float[] rotatedSquare = new Point2D.Float[4];
        double radians = Math.toRadians(180 - degrees);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        for (int i = 0; i < 4; i++) {
            float x = square[i].x - centerX;
            float y = square[i].y - centerY;

            float rotatedX = (float) (x * cosTheta - y * sinTheta) + centerX;
            float rotatedY = (float) (x * sinTheta + y * cosTheta) + centerY;

            rotatedSquare[i] = new Point2D.Float(rotatedX, rotatedY);
        }

        return rotatedSquare;
    }


    public int getX2() {
        return getX() + getWidth();
    }

    public int getY2() {
        return getY() + getHeight();
    }

    public void setRotate(float i) {
        style.styles.put(ViewStyles.ROTATE, Float.toString(i));
    }

    public Float getRotate() {
        return style.get(ViewStyles.ROTATE, Float.class);
    }

//    public void setY2(float i) {
//        style.styles.put(ViewStyles.Y2, Float.toString(i));
//    }

    public void setBackground(String color) {
        style.styles.put(ViewStyles.BACKGROUND, color);
    }

    public static class ViewStyles extends RenderableStyles{
        public static final String BACKGROUND = "background-color";

        public static final String ROTATE = "position-rotate";
        public static final String ROTATE_LOCK_X = "position-lock-x";
        public static final String ROTATE_LOCK_Y = "position-lock-y";

        public ViewStyles(){
            addHandler(BACKGROUND, "#00ffffff", int.class, ViewStyles::hexToRGBA);


            addHandler(ROTATE, "null", Float.class, Float::parseFloat);
            addHandler(ROTATE_LOCK_X, "false", Boolean.class, Boolean::parseBoolean);
            addHandler(ROTATE_LOCK_Y, "false", Boolean.class, Boolean::parseBoolean);

        }



        public static int hexToRGBA(String colorHexString) {

            String local = colorHexString;
            int a;
            int r;
            int g;
            int b;
            if (local.startsWith("#")) {
                local = local.substring(1, local.length());
            }
            int len = local.length();

            if (len > 8) {
                // log error.
                return Colors.RED;
            } // end if

            String rgbSub;
            try {
                // Alpha
                if (8 == len) {
                    int h = hexCharToInt(local.charAt(0));
                    int l = hexCharToInt(local.charAt(1));
                    a = h * 16 + l;
                    rgbSub = local.substring(2, len);
                } else if (7 == len) {
                    int h = 0xf;
                    int l = hexCharToInt(local.charAt(0));
                    a = h * 16 + l;
                    rgbSub = local.substring(1, len);
                } else {
                    a = 255;
                    rgbSub = local;
                }
                // Red
                {
                    int h = hexCharToInt(rgbSub.charAt(0));
                    int l = hexCharToInt(rgbSub.charAt(1));
                    r = h * 16 + l;
                }
                // Green
                {
                    int h = hexCharToInt(rgbSub.charAt(2));
                    int l = hexCharToInt(rgbSub.charAt(3));
                    g = h * 16 + l;
                }
                // Blue
                {
                    int h = hexCharToInt(rgbSub.charAt(4));
                    int l = hexCharToInt(rgbSub.charAt(5));
                    b = h * 16 + l;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return Colors.RED;
            }

            return ColorHelper.Argb.getArgb(a, r, g, b);
        }
        private static int hexCharToInt(char ch) {
            if (ch >= '0' && ch <= '9') {
                return ch - '0';
            } else if (ch >= 'a' && ch <= 'z') {
                return ch - 97 + 10;
            } else if (ch >= 'A' && ch <= 'Z') {
                return ch - 65 + 10;
            } else {
                throw new IllegalArgumentException("Char Not Hex");
            }
        }
    }

}
