package spcore.view.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import spcore.GlobalContext;
import spcore.view.Component;
import spcore.view.styles.ComponentStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class Renderable implements Component, Drawable {

    protected boolean visible = true;
    public final List<WrappedElement> children = new ArrayList<>();
    private Renderable parent;

    protected RenderableStyles style;

    protected Renderable(Vector2i position) {
        this.style = new RenderableStyles();
        this.setX(Float.toString(position.x));
        this.setY(Float.toString(position.y));
    }

    public Vector2i getPosition(){
        return new Vector2i((int)getX(), (int)getY());
    }
    public RenderableStyles getStyle(){
        return style;
    }

    public void configure(Renderable parent, RenderContext render){
        this.parent = parent;
        if(parent != null){
            this.style.styles.put(RenderableStyles.PARENT_WIDTH, Float.toString(parent.getWidth()));
            this.style.styles.put(RenderableStyles.PARENT_HEIGHT, Float.toString(parent.getHeight()));
        }
        else{
            this.style.styles.put(RenderableStyles.PARENT_WIDTH, this.style.styles.get(RenderableStyles.WIDTH));
            this.style.styles.put(RenderableStyles.PARENT_HEIGHT, this.style.styles.get(RenderableStyles.HEIGHT));
        }
        internalConfigure(render);
        for (var child: children
             ) {
            child.renderable.configure(this, render);
        }
    }

    public void cache(){
//        this.style.cache();
//        for (var child: children
//        ) {
//            child.renderable.cache();
//        }
    }

    public void addChild(Renderable renderable, Positioner positioner){
        children.add(new WrappedElement(renderable, positioner));
    }

    public void addChild(Renderable renderable){
        children.add(new WrappedElement(renderable, Positioner.create()));
    }

    @Override
    public void forEachChild(Consumer<Renderable> consumer) {
        for (var child: children
        ) {
            consumer.accept(child.renderable);
        }
    }

    public void forEachAllChild(Consumer<Renderable> consumer) {
        consumer.accept(this);
        for (var child: children
        ) {
            child.renderable.forEachAllChild(consumer);
        }
    }

    protected abstract void internalConfigure(RenderContext render);
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        if(!visible)
            return;

        internalRender(context, mouseX, mouseY, delta);
    }

    protected abstract void internalRender(DrawContext context, int mouseX, int mouseY, float delta);

    @Override
    public void setX(String x) {
        style.styles.put(RenderableStyles.POS_X1, x);
        float bx = style.get(RenderableStyles.POS_X1, float.class);
        this.forEachChild(element -> {
            float j = element.getX() + bx;
            element.setX(Float.toString(j));
        });

    }

    @Override
    public void setY(String y) {
        style.styles.put(RenderableStyles.POS_Y1, y);
        float by = style.get(RenderableStyles.POS_Y1, float.class);
        this.forEachChild(element -> {
            float j = element.getY() + by;
            element.setY(Float.toString(j));
        });
    }

    @Override
    public float getX() {
        return style.get(RenderableStyles.POS_X1, float.class);
    }

    @Override
    public float getY() {
        return style.get(RenderableStyles.POS_Y1, float.class);
    }

    @Override
    public float getWidth() {
        return style.get(RenderableStyles.WIDTH, float.class);
    }

    @Override
    public float getHeight() {
        return style.get(RenderableStyles.HEIGHT, float.class);
    }

    public void setWidth(float i) {
        style.styles.put(RenderableStyles.WIDTH, Float.toString(i));
    }

    public void setHeight(float i) {
        style.styles.put(RenderableStyles.HEIGHT, Float.toString(i));
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Renderable getParent() {
        return parent;
    }

    public float getFloatStyle(String id){
        return style.get(id, float.class);
    }

    public void setFloatStyle(String id, float value){
        //GlobalContext.LOGGER.info(id + ": " + value);
        style.styles.put(id, Float.toString(value));
    }

    public Float getFloatData(String id){
        var v = style.data.get(id);
        if(v == null){
            return null;
        }
        return (float) v;
    }

    public void setFloatData(String id, float value){
        style.data.put(id, value);
    }


    public void setParent(Renderable parent) {
        this.parent = parent;
    }

    public void refreshPositions() {
        this.forEachChild(Renderable::refreshPositions);
    }

    protected static class WrappedElement{
        public final Positioner.Impl positioner;
        public final Renderable renderable;
        public WrappedElement(Renderable renderable, Positioner positioner) {
            this.renderable = renderable;
            this.positioner = positioner.toImpl();
        }

        public float getHeight() {
            return this.renderable.getHeight() + this.positioner.marginTop + this.positioner.marginBottom;
        }

        public float getWidth() {
            return this.renderable.getWidth() + this.positioner.marginLeft + this.positioner.marginRight;
        }

        public void setX(float left, float right) {
            float f = (float)this.positioner.marginLeft;
            float g = right - this.renderable.getWidth() - this.positioner.marginRight;
            int i = (int) MathHelper.lerp(this.positioner.relativeX, f, g);
//            var x = i + left;
//            var parent_w = this.renderable.style.get(RenderableStyles.PARENT_WIDTH, int.class);
            this.renderable.setX(Float.toString(i + left));
        }

        public void setY(float top, float bottom) {
            float f = (float)this.positioner.marginTop;
            float g = bottom - this.renderable.getHeight() - this.positioner.marginBottom;
            int i = Math.round(MathHelper.lerp(this.positioner.relativeY, f, g));
            this.renderable.setY(Float.toString(i + top));
        }
    }

    public static class RenderableStyles extends ComponentStyle {
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String PARENT_WIDTH = "parent_width";
        public static final String PARENT_HEIGHT = "parent_height";
        public static final String LAYER = "layer";

        public static final String POS_X1 = "position-x1";
        public static final String POS_Y1 = "position-y1";
        public RenderableStyles(){
            addHandler(WIDTH, "5", float.class, Float::parseFloat);
            addHandler(HEIGHT, "5", float.class, Float::parseFloat);
            addHandler(PARENT_WIDTH, "5", float.class, Float::parseFloat);
            addHandler(PARENT_HEIGHT, "5", float.class, Float::parseFloat);
            addHandler(LAYER, "0", int.class, Integer::parseInt);

            addHandler(POS_X1, "0", float.class, p -> {
                if(p.endsWith("%")){
                    var w = get(RenderableStyles.PARENT_WIDTH, float.class);
                    if(w == null){
                        w = get(RenderableStyles.WIDTH, float.class);
                    }
                    return calc(p, 0, w);
                }
                return Float.parseFloat(p);
            });
            addHandler(POS_Y1, "0", float.class, p -> {
                if(p.endsWith("%")){
                    var w = get(RenderableStyles.PARENT_HEIGHT, float.class);
                    if(w == null){
                        w = get(RenderableStyles.HEIGHT, float.class);
                    }
                    return calc(p, 0, w);
                }
                return Float.parseFloat(p);
            });
        }

        public float calc(String v, float d, float max){
            v = v.replaceAll("%", "");
            if(v.equals("init")){
                return d;
            }

            var i = Float.parseFloat(v);
            if(i == 0){
                return 0;
            }

            if(i < 0){
                i = 100 + i;
            }

            var one = max / 100.0;
            return (float) (one * i);
        }
    }

}
