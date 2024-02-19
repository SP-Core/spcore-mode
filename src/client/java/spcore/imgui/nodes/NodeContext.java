package spcore.imgui.nodes;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorConfig;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.type.ImInt;
import net.minecraft.client.gui.screen.Screen;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import spcore.GlobalContext;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.utils.ImageLoader;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeContext {

    public final NodeEditorContext context;
    private final String viewName;
    private final NodeEditorConfig config;
    public List<Node> nodes;
    public List<Link> links;
    public final NodeRender render;
    public final int textureId = 0;
    public int width;
    public int height;
    private int lastId = 1;
    public boolean init = false;
    public final Screen screen;

    public NodeContext(String viewName, Screen screen) throws IOException {
        this.viewName = viewName;
        this.screen = screen;
        load();
        this.config = new NodeEditorConfig();
        this.config.setSettingsFile("studio-views/" + viewName + ".blueprint.json");
        this.context = new NodeEditorContext(config);
        this.render = new NodeRender(this);

    }


    public int nextId(){
        return lastId++;
    }


    public void init(){

        if(!init){
            NodeEditor.setCurrentEditor(context);
            init = true;
        }
    }


    public boolean save(){
        var vf = new ViewFile();
        vf.lastId = this.lastId;
        vf.nodes = this.nodes;
        vf.links = this.links;
        var mapper = new ObjectMapper(new Gson());
        var json = mapper.writeValueAsString(vf);
        var mainFolder = new File("studio-views");
        var hashFile = new File(mainFolder, viewName + ".hash");
        try {
            if(Files.exists(hashFile.toPath())){
                var lastHash = Files.readString(hashFile.toPath());
                if(Integer.parseInt(lastHash) == json.hashCode()){
                    return false;
                }
            }

            var viewFile = new File(mainFolder, viewName + ".view.json");

            Files.writeString(viewFile.toPath(),json);
            Files.writeString(hashFile.toPath(), Integer.toString(json.hashCode()));
        } catch (IOException e) {
            GlobalContext.LOGGER.error(e.getMessage());
        }

        return true;
    }

    public void load() throws IOException {
        var mainFolder = new File("studio-views");
        if(!Files.exists(mainFolder.toPath())){
            Files.createDirectories(mainFolder.toPath());
        }

        var viewFile = new File(mainFolder, viewName + ".view.json");
        if(!Files.exists(viewFile.toPath())){
            this.nodes = new ArrayList<>();
            this.links = new ArrayList<>();
            this.lastId = 1;
            save();
            return;
        }

        var json = Files.readString(viewFile.toPath());
        var mapper = new ObjectMapper(new Gson());
        var vf = mapper.readValue(json, ViewFile.class);
        this.nodes = vf.nodes;
        this.lastId = vf.lastId;
        this.links = vf.links;

        var hashFile = new File(mainFolder, viewName + ".hash");
        Files.writeString(hashFile.toPath(), Integer.toString(json.hashCode()));
    }



    private static class ViewFile{
        public List<Node> nodes;
        public List<Link> links;
        private int lastId;

    }

    public Renderable startProcess(boolean debugShow){

        var data = new ProcessService.ProcessData();
        var devRenderNodes =
                nodes.stream().filter(p -> p.name.equals("Dev render"))
                        .toList();

        var devNodeType = render.nodeTypes
                .stream().filter(p -> p.getName().equals("Dev render"))
                .findAny().get();

        List<Renderable> devComponents = new ArrayList<>();
        for (var node: devRenderNodes
        ) {
            var ps = new ProcessService(node, render, data);
            var out = devNodeType.process(node, ps);
            devComponents.add((Renderable)out.get("out"));
        }

        var renderNodes =
                nodes.stream().filter(p -> p.name.equals("render"))
                        .toList();

        var nodeType = render.nodeTypes
                .stream().filter(p -> p.getName().equals("render"))
                .findAny().get();

        var view = new ViewComponent();
        for (var node: renderNodes
             ) {
            var ps = new ProcessService(node, render, data);
            var out = nodeType.process(node, ps);
            view.addChild((Renderable)out.get("out"));
        }

        if(debugShow){
            for (var devComponent: devComponents
            ) {
                view.addChild(devComponent);
            }
        }


        return view;
    }

//     try {
//
//        textureId = loadTexture("/gui/blueprint/BlueprintBackground.png");
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }

    //    public int loadTexture(String name) throws IOException {
////        var r = ImGuiImpl.class
////                .getResourceAsStream(name);
////        byte[] bytes = IOUtils.toByteArray(r);
////        var buffer = ByteBuffer.wrap(bytes);
////
////        ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(buffer, BufferUtils.createIntBuffer(1), BufferUtils.createIntBuffer(1), BufferUtils.createIntBuffer(1), 4);
//
//        width = 1;
//        height = 1;
////        int channels = imageBuffer.getInt();
//
////        return 1; ImageLoader.loadImageFromImageBuffer(buffer, width, height);
//        return 1;
//    }

}
