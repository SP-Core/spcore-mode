package spcore.view;

import spcore.view.events.MousePositionEvent;

import java.util.ArrayList;
import java.util.List;

public class ComponentEvents {
    public final List<MousePositionEvent> onClick = new ArrayList<>();
    public final List<MousePositionEvent> onRelease = new ArrayList<>();

    public void click(double mouseX, double mouseY){
        for (var e :onClick
             ) {
            e.invoke(mouseX, mouseY);
        }
    }

    public void release(double mouseX, double mouseY){
        for (var e :onRelease
        ) {
            e.invoke(mouseX, mouseY);
        }
    }
}
