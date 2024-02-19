package spcore.view.layouts;

import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.util.math.MathHelper;
import spcore.view.render.Renderable;

import java.util.ArrayList;
import java.util.List;

public class AdderComponent extends GridComponent {
    private int columns;
    private int totalOccupiedColumns;


    public final List<GridElementContainer> elements = new ArrayList<>();
    public void setColumns(int columns){
        this.columns = columns;
    }

    public <T extends Renderable> GridElementContainer add(T widget) {
        return this.add(widget, 1);
    }

    public <T extends Renderable> GridElementContainer add(T widget, int occupiedColumns) {
        return this.add(widget, occupiedColumns, this.getMainPositioner());
    }

    public <T extends Renderable> GridElementContainer add(T widget, Positioner positioner) {
        return this.add(widget, 1, positioner);
    }

    public <T extends Renderable> GridElementContainer add(T widget, int occupiedColumns, Positioner positioner) {
        int i = this.totalOccupiedColumns / this.columns;
        int j = this.totalOccupiedColumns % this.columns;
        if (j + occupiedColumns > this.columns) {
            ++i;
            j = 0;
            this.totalOccupiedColumns = MathHelper.roundUpToMultiple(this.totalOccupiedColumns, this.columns);
        }

        this.totalOccupiedColumns += occupiedColumns;

        var wrapComponent = new GridElementContainer();
        var pos = widget.getPosition();
        wrapComponent.addChild(widget, positioner);
        wrapComponent.setX(Float.toString(pos.x));
        wrapComponent.setY(Float.toString(pos.y));
        wrapComponent.getStyle().styles.put(GridElementContainer.GridElementStyles.ROW, Integer.toString(i));
        wrapComponent.getStyle().styles.put(GridElementContainer.GridElementStyles.COLUMN, Integer.toString(j));
        wrapComponent.getStyle().styles.put(GridElementContainer.GridElementStyles.OCCUPIED_ROWS, Integer.toString(1));
        wrapComponent.getStyle().styles.put(GridElementContainer.GridElementStyles.OCCUPIED_COLUMNS, Integer.toString(occupiedColumns));
        elements.add(wrapComponent);
        return wrapComponent;
    }


}

