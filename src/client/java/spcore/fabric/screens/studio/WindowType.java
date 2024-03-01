package spcore.fabric.screens.studio;


import spcore.fabric.screens.studio.windows.AbstractWindow;
import spcore.fabric.screens.studio.windows.GuiNodesWindow;
import spcore.fabric.screens.studio.windows.ObjectInspector;

import java.util.HashMap;

public enum WindowType {
    ObjectInspector("ObjectInspector", WindowCategory.Global, new WindowFactory<ObjectInspector>()),
    GuiNodes("GuiNode", WindowCategory.GuiNodes, new WindowFactory<GuiNodesWindow>());
    public final String name;
    public final WindowCategory category;
    public final WindowFactory nodeType;
    WindowType(String name, WindowCategory category, WindowFactory nodeType){
        this.name = name;
        this.category = category;
        this.nodeType = nodeType;
    }

}
