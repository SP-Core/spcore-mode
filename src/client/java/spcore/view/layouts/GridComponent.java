package spcore.view.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.util.math.Divider;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import spcore.view.Component;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.ArrayList;
import java.util.List;

public class GridComponent extends ViewComponent {

    private final Positioner mainPositioner = Positioner.create();

    public GridComponent() {
        style = new GridComponentStyles();

    }


    @Override
    public void refreshPositions() {
        super.refreshPositions();
        int i = 0;
        int j = 0;

        for(var element : this.children) {
            if(element instanceof Element e){
                i = Math.max(e.getRowEnd(), i);
                j = Math.max(e.getColumnEnd(), j);
            }
        }

        int[] is = new int[j + 1];
        int[] js = new int[i + 1];

        for(var e : this.children) {
            if(e instanceof Element element2 && element2.renderable instanceof GridElementContainer container){
                int k = element2.getHeight() - (container.getOccupiedRows() - 1) * this.getRowSpacing();
                Divider divider = new Divider(k, container.getOccupiedRows());

                for(int l = container.getRow(); l <= element2.getRowEnd(); ++l) {
                    js[l] = Math.max(js[l], divider.nextInt());
                }

                int l = element2.getWidth() - (container.getOccupiedColumns() - 1) * this.getColumnSpacing();
                Divider divider2 = new Divider(l, container.getOccupiedColumns());

                for(int m = container.getColumn(); m <= element2.getColumnEnd(); ++m) {
                    is[m] = Math.max(is[m], divider2.nextInt());
                }
            }
        }

        int[] ks = new int[j + 1];
        int[] ls = new int[i + 1];
        ks[0] = 0;

        for(int k = 1; k <= j; ++k) {
            ks[k] = ks[k - 1] + is[k - 1] + this.getColumnSpacing();
        }

        ls[0] = 0;

        for(int k = 1; k <= i; ++k) {
            ls[k] = ls[k - 1] + js[k - 1] + this.getRowSpacing();
        }

        for(var e : this.children) {
            if(e instanceof Element element3 && element3.renderable instanceof GridElementContainer container){
                int l = 0;

                for(int n = container.getColumn(); n <= element3.getColumnEnd(); ++n) {
                    l += is[n];
                }

                l += this.getColumnSpacing() * (container.getOccupiedColumns() - 1);
                if(ks.length > container.getColumn()){
                    element3.setX(this.getX() + ks[container.getColumn()], l);
                }

                int n = 0;

                for(int m = container.getRow(); m <= element3.getRowEnd(); ++m) {
                    n += js[m];
                }

                n += this.getRowSpacing() * (container.getOccupiedRows() - 1);
                if(ls.length > container.getRow()){
                    element3.setY(this.getY() + ls[container.getRow()], n);
                }

            }

        }

        this.style.styles.put(ViewStyles.WIDTH, Integer.toString(ks[j] + is[j]));
        this.style.styles.put(ViewStyles.HEIGHT, Integer.toString(ls[i] + js[i]));
    }


    public GridComponent setColumnSpacing(int columnSpacing) {
        this.style.styles.put(GridComponentStyles.COLUMN_SPACING, Integer.toString(columnSpacing));
        return this;
    }

    public GridComponent setRowSpacing(int rowSpacing) {
        this.style.styles.put(GridComponentStyles.ROW_SPACING, Integer.toString(rowSpacing));
        return this;
    }
    public GridComponent setSpacing(int spacing) {
        return this.setColumnSpacing(spacing).setRowSpacing(spacing);
    }


    public int getColumnSpacing() {
        return this.style.get(GridComponentStyles.COLUMN_SPACING, int.class);
    }

    public int getRowSpacing() {
        return this.style.get(GridComponentStyles.ROW_SPACING, int.class);
    }

    public Positioner getMainPositioner() {
        return mainPositioner;
    }
    public Positioner copyPositioner() {
        return this.mainPositioner.copy();
    }

    public static class Element extends Renderable.WrappedElement {

        protected Element(GridElementContainer renderable, Positioner positioner) {
            super(renderable, positioner);
        }

        public int getRowEnd() {
            var style = this.renderable.getStyle();
            return style.get(GridElementContainer.GridElementStyles.ROW, int.class) +
                    style.get(GridElementContainer.GridElementStyles.OCCUPIED_ROWS, int.class) - 1;
        }

        public int getColumnEnd() {
            var style = this.renderable.getStyle();

            return style.get(GridElementContainer.GridElementStyles.COLUMN, int.class) +
                    style.get(GridElementContainer.GridElementStyles.OCCUPIED_COLUMNS, int.class) - 1;
        }


    }

    @Override
    public void addChild(Renderable renderable) {
        if(renderable instanceof AdderComponent adder){
            for (var element: adder.elements
                 ) {
                super.children.add(new Element(element, getMainPositioner()));
//                super.addChild(new Element(element, getMainPositioner()));
            }
            return;
        }
        super.addChild(renderable);
    }

    static class GridComponentStyles extends ViewStyles{

        public final static String ROW_SPACING = "row_spacing";
        public final static String COLUMN_SPACING = "column_spacing";


        GridComponentStyles(){
            addHandler(ROW_SPACING, "1", int.class, Integer::parseInt);
            addHandler(COLUMN_SPACING, "1", int.class, Integer::parseInt);
        }
    }
}

