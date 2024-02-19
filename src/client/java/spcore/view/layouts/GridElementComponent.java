package spcore.view.layouts;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import spcore.view.render.RenderContext;
import spcore.view.render.Renderable;

public class GridElementComponent {

    private int columns;

    private int occupiedColumns;
    private Renderable renderable;


    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }


    public Renderable getRenderable() {
        return renderable;
    }

    public void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }

    public boolean isEmpty(){
        return renderable == null;
    }

    public int getOccupiedColumns() {
        return occupiedColumns;
    }

    public void setOccupiedColumns(int occupiedColumns) {
        this.occupiedColumns = occupiedColumns;
    }
}
