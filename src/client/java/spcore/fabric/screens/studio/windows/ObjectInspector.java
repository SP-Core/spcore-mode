package spcore.fabric.screens.studio.windows;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import spcore.fabric.screens.studio.StudioView;
import spcore.fabric.screens.studio.WindowType;
import spcore.imgui.nodes.NodeTab;

public class ObjectInspector extends AbstractWindow{


    public ImString createView = new ImString("", 20);
    public boolean showCreateView = false;

    @Override
    public String getName() {
        return "Object Inspector";
    }

    @Override
    public String getId() {
        return "Object Inspector";
    }

    @Override
    public void render() {
        createViewPopup();
        var xOne = ImGui.getMainViewport().getSizeX() / 100.0;
        var yOne = ImGui.getMainViewport().getSizeY() / 100.0;
        ImGui.setNextWindowSize((int)(xOne * 20), (int)(yOne * 60), ImGuiCond.Once);
        if (ImGui.begin(getName(), isShow, ImGuiWindowFlags.NoCollapse)){

            if(ImGui.treeNode("views")){

                if(ImGui.button("create")){
                    openCreateView();
                }
                for (var v: getScreen().viewsSettings.views
                ) {
                    var b = new ImBoolean(getScreen().windows.containsKey(v.name));
                    if(ImGui.checkbox(v.name, b)){
                        if(b.get()){
                            var w = getScreen().createWindow(
                                    GuiNodesWindow.class,
                                    WindowType.GuiNodes);

                            w.setData(GuiNodesWindow.VIEW, v);
                            getScreen().toggleWindow(w);
                        }
                        else{
                            getScreen().windows.remove(v.name);
                        }
                    }
                }
                ImGui.treePop();
            }

            ImGui.end();
        }


    }

    public void openCreateView(){
        createView = new ImString("", 20);
        showCreateView = true;
    }


    public void createViewPopup(){

        if(showCreateView){
            ImGui.openPopup("Create view");
        }
        ImVec2 center = ImGui.getMainViewport().getCenter();
        ImGui.setNextWindowPos(center.x, center.y, ImGuiCond.Appearing, 0.5f, 0.5f);

        if(ImGui.beginPopupModal("Create view", ImGuiWindowFlags.AlwaysAutoResize)){
            ImGui.inputText("name", createView);

            if (ImGui.button("OK", 120, 0))
            {
                var v = getScreen().addView(createView.get());
                var w = getScreen().createWindow(
                        GuiNodesWindow.class,
                        WindowType.GuiNodes);

                w.setData(GuiNodesWindow.VIEW, v);
                getScreen().toggleWindow(w);

                ImGui.closeCurrentPopup();
                showCreateView = false;
            }
            ImGui.setItemDefaultFocus();
            ImGui.sameLine();
            if (ImGui.button("Cancel", 120, 0))
            {
                ImGui.closeCurrentPopup();
                showCreateView = false;
            }
            ImGui.endPopup();
        }
    }

    @Override
    public void init() {

    }
}
