package spcore.imgui.nodes;

import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorConfig;
import imgui.extension.nodeditor.NodeEditorContext;
import net.minecraft.client.gui.screen.Screen;
import spcore.engine.models.ViewController;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.SetterInfo;
import spcore.imgui.nodes.models.ViewScope;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NodeContext {

    public final NodeEditorContext context;
    private final NodeEditorConfig config;
    public List<Node> nodes;
    public List<Link> links;
    public final NodeRender render;
    public final int textureId = 0;
    public int width;
    public int height;
    private int lastId = 1;
    public boolean init = false;
    public ViewScope scope;
    private final NodeProvider nodeProvider;

    public HashMap<String, List<SetterInfo>> setters = new HashMap<>();
    public HashMap<String, List<Renderable>> last_wrappers = new HashMap<>();
    public final Optional<ViewController> viewController;
    public NodeContext(Optional<ViewController> viewController, NodeProvider nodeProvider, String blueprintPath) throws IOException {
        this.viewController = viewController;
        this.nodeProvider = nodeProvider;
        load();
        this.config = new NodeEditorConfig();
        this.config.setSettingsFile(blueprintPath);
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
        var vf = new NodeProvider.ViewFile();
        vf.lastId = this.lastId;
        vf.nodes = this.nodes;
        vf.links = this.links;

        return nodeProvider.save(vf);
    }

    public void load() throws IOException {

        var f = nodeProvider.load();
        this.nodes = f.nodes;
        this.links = f.links;
        this.lastId = f.lastId;

        for (var node: nodes
        ) {
            node.init();
        }


    }


    public void setter(String id, String key, String value){
        if(setters.containsKey(id)){
            setters.get(id).add(new SetterInfo(id, key, value));
        }
        else{
            setters.put(id, new ArrayList<>());
            setters.get(id).add(new SetterInfo(id, key, value));
        }
    }


    public Renderable startProcess(boolean debugShow, ViewScope scope){

        this.scope = scope;
        var data = new ProcessService.ProcessData();

        var devRenderNodes =
                nodes.stream().filter(p -> p.innerType == NodeType.DevRender)
                        .toList();

        var devNodeType = NodeType.DevRender.nodeType;

        List<Renderable> devComponents = new ArrayList<>();
        for (var node: devRenderNodes
        ) {
            var ps = new ProcessService(node, render, data);
            var out = devNodeType.process(node, ps);
            devComponents.add((Renderable)out.get("out"));
        }

        var renderNodes =
                nodes.stream().filter(p -> p.innerType == NodeType.Render)
                        .toList();

        var nodeType = NodeType.Render.nodeType;

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

        this.last_wrappers = data.wrappers;

        return view;
    }



}
