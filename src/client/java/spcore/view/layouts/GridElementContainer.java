package spcore.view.layouts;

import org.joml.Vector2i;
import spcore.view.ViewComponent;

import java.awt.*;

public class GridElementContainer extends ViewComponent {
    protected GridElementContainer() {
        style = new GridElementStyles();
    }

    public int getRow(){
        return style.get(GridElementStyles.ROW, int.class);
    }

    public int getColumn(){
        return style.get(GridElementStyles.COLUMN, int.class);
    }

    public int getOccupiedRows(){
        return style.get(GridElementStyles.OCCUPIED_ROWS, int.class);
    }

    public int getOccupiedColumns(){
        return style.get(GridElementStyles.OCCUPIED_COLUMNS, int.class);
    }

    protected static class GridElementStyles extends ViewStyles{
        public static final String ROW = "row";
        public static final String COLUMN = "column";
        public static final String OCCUPIED_ROWS = "occupiedRows";
        public static final String OCCUPIED_COLUMNS = "occupiedColumns";
        public GridElementStyles(){
            addHandler(ROW, "1", int.class, Integer::parseInt);
            addHandler(COLUMN, "1", int.class, Integer::parseInt);
            addHandler(OCCUPIED_ROWS, "1", int.class, Integer::parseInt);
            addHandler(OCCUPIED_COLUMNS, "1", int.class, Integer::parseInt);
        }
    }
}
