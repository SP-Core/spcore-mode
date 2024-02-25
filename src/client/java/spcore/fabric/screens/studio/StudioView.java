package spcore.fabric.screens.studio;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import imgui.ImColor;
import imgui.ImGui;
import imgui.extension.nodeditor.NodeEditor;
import imgui.flag.*;
import imgui.internal.flag.ImGuiDockNodeFlags;
import imgui.type.ImBoolean;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import spcore.GlobalContext;
import spcore.appapi.helpers.PathHelper;
import spcore.appapi.models.SpCoreInfo;
import spcore.engine.TimeApi;
import spcore.engine.ViewContext;
import spcore.engine.models.ViewsInfo;
import spcore.fabric.screens.WrapperedScreen;
import spcore.fabric.screens.studio.windows.AbstractWindow;
import spcore.fabric.screens.studio.windows.GuiNodesWindow;
import spcore.fabric.screens.studio.windows.ObjectInspector;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.NodeProvider;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.ViewScope;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.providers.DebugProvider;
import spcore.js.JsRuntime;
import spcore.view.components.TextComponent;
import spcore.view.render.RenderEngine;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StudioView extends WrapperedScreen {
    public final HashMap<String, AbstractWindow> windows = new HashMap<>();
    public final ViewsInfo viewsSettings;
    public final SpCoreInfo manifest;
    public File viewsFile;
    public boolean debugShow = true;
    private ImBoolean showMetrics = new ImBoolean(false);
    public StudioView(ViewContext viewContext, SpCoreInfo manifest, String currentView, File viewsFile) {
        super(viewContext, Text.empty());
        GuiNodesWindow.destroy();
        this.manifest = manifest;
        this.viewsFile = viewsFile;
        var mapper = new ObjectMapper(new Gson());
        String contentViews = null;
        try {
            contentViews = Files.readString(viewsFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        viewsSettings = mapper.readValue(contentViews, ViewsInfo.class);
        var view = viewsSettings.views
                .stream()
                .filter(p -> p.name.equals(currentView))
                .findFirst()
                .get();

        var w = createWindow(
                GuiNodesWindow.class,
                WindowType.GuiNodes);

        w.setData(GuiNodesWindow.VIEW, view);
        toggleWindow(w);
        GuiNodesWindow.setCurrentNode(view.name);
    }

    public void removeView(String name){
        var m = viewsSettings.views
                .stream().filter(p -> p.name.equals(name))
                .findFirst();
        m.ifPresent(view -> viewsSettings.views.remove(view));

        var mapper = new ObjectMapper(new Gson());

        var json = mapper.writeValueAsString(viewsSettings);

        try {
            Files.writeString(viewsFile.toPath(), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ViewsInfo.View addView(String name){
        var v = new ViewsInfo.View();
        v.name = name;
        var absPath = viewsFile.getParentFile().getAbsolutePath();
        var folderFile = PathHelper.combineFile(absPath, name);
        if(!Files.exists(folderFile.toPath())){
            folderFile.mkdirs();
        }

        var viewFile = PathHelper.combineFile(folderFile.getAbsolutePath(), name + ".view.json");
        if(!viewFile.exists()){
            try {
                Files.writeString(viewFile.toPath(), "{\"nodes\":[], \"links\": [], \"lastId\":0}", StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        v.path = "/" + name + "/" + name + ".view.json";
        v.blueprint = "/" + name + "/" + name + ".blueprint.json";
        viewsSettings.views.add(v);
        var mapper = new ObjectMapper(new Gson());

        var json = mapper.writeValueAsString(viewsSettings);

        try {
            Files.writeString(viewsFile.toPath(), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return v;
    }


    @Override
    protected void init(){
        super.init();

        for (var w: windows.entrySet()
             ) {
            w.getValue().init();
        }
    }

    public void restart(){
        super.clearAndInit();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TimeApi.start();

        var cc = GuiNodesWindow.getCurrentContext();
        if(cc != null && cc.viewController.isPresent()){
            try {
                super.renderWrappers(cc.viewController.get().index);
            } catch (ScriptException e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }


        super.render(context, mouseX, mouseY, delta);
        TimeApi.end();

        ImGuiImpl.draw(io -> {
            if(showMetrics.get()){
                ImGui.showMetricsWindow(showMetrics);
            }
            if(!this.debugShow){
                return;
            }

            var wp = ImGui.getMainViewport().getWorkPos();
            var ws = ImGui.getMainViewport().getWorkSize();
            var window_flags =
                    ImGuiWindowFlags.NoDocking |
                            ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                            ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                            ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus |
                            ImGuiWindowFlags.NoBackground;

            ImGui.setNextWindowPos(wp.x, wp.y);
            ImGui.setNextWindowSize(ws.x, ws.y);
            ImGui.setNextWindowDockID(ImGui.getMainViewport().getID());
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f); // No corner rounding on the window
            ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f); // No border around the window
            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);

            if(ImGui.begin("main", window_flags)){
                var dockspace_id = ImGui.getID("MyDockSpace");
                ImGui.pushStyleColor(ImGuiCol.WindowBg, 0,0,0,0);
                ImGui.dockSpace(dockspace_id, 0.0f, 0.0f, ImGuiDockNodeFlags.PassthruCentralNode);
                ImGui.popStyleColor();
                if(ImGui.isMouseClicked(ImGuiMouseButton.Right)){
                    ImGui.openPopup("Main popup");
                }
                createPopup();
                ImGui.end();
            }
            ImGui.popStyleVar();
            ImGui.popStyleVar(2);

            var iterator = windows.entrySet().iterator();
            while (iterator.hasNext()){
                var w = iterator.next();
                if(!w.getValue().isShow()){
                    iterator.remove();
                    continue;
                }
                w.getValue().render();
            }

        });


    }

    public void createPopup(){
        if(ImGui.beginPopup("Main popup")){
            var oi = new ImBoolean(windows.containsKey("Object Inspector"));
            if(ImGui.menuItem("Object inspector", "", oi)){
                if(oi.get()){
                    var w = createWindow(ObjectInspector.class, WindowType.ObjectInspector);
                    toggleWindow(w);
                }
                else{
                    windows.remove("Object Inspector");
                }
            }

            if (ImGui.menuItem("Metrics", null))
            {
                showMetrics.set(true);
            }
            ImGui.endPopup();
        }
    }


    public void pAddDrawable(Drawable drawable){
        super.addDrawable(drawable);
    }

    public boolean toggleWindow(AbstractWindow window){
        window.toggle();
        if(window.getId() == null){
            return false;
        }

        if(windows.containsKey(window.getId())){
            return false;
        }

        windows.put(window.getId(), window);

        return true;
    }

    public <T extends AbstractWindow> T createWindow(Class<T> clazz, WindowType type){
        return (T) type.nodeType.create(clazz, this);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 258 && scanCode == 15 && modifiers == 0){
            this.debugShow = !debugShow;
            this.clearAndInit();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
