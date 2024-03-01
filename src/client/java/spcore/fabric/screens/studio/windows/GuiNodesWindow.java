package spcore.fabric.screens.studio.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import spcore.GlobalContext;
import spcore.appapi.helpers.PathHelper;
import spcore.engine.models.ViewController;
import spcore.engine.models.ViewsInfo;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.NodeProvider;
import spcore.imgui.nodes.models.ViewScope;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.providers.DebugProvider;
import spcore.imgui.nodes.providers.ProductionProvider;
import spcore.view.render.RenderEngine;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GuiNodesWindow extends AbstractWindow{

    public static final String PROVIDER = "provider";
    public static final String VIEW_NAME = "view_name";
    private static final HashMap<String, NodeContext> nodeContexts = new HashMap<>();

    private static String currentNode;
    private static String nextCurrentNode;

    public static void setCurrentNode(String node){
        currentNode = node;
    }
    public static void destroy(){
        nodeContexts.clear();
        currentNode = null;
        nextCurrentNode = null;
    }

    public static NodeContext getCurrentContext(){
        return nodeContexts.get(currentNode);
    }

    public static Collection<NodeContext> getContexts(){
        return nodeContexts.values();
    }

    public static NodeContext getOrCreateContext(String key, ViewScope scope){

        if(nodeContexts.containsKey(key)){
            return nodeContexts.get(key);
        }

        var viewController = scope.viewContext.viewControllers.stream()
                .filter(p -> p.name.equals(key))
                .findFirst();

        NodeProvider provider;
        if(scope.viewsInfo == null){
            if(viewController.isEmpty()){
                return null;
            }
            provider = new ProductionProvider(key, viewController.get().nodes, viewController.get().links, viewController.get().lastId);
        }
        else{
            var view = scope.viewsInfo
                    .views.stream().filter(p -> p.name.equals(key))
                    .findFirst();

            if(view.isEmpty()){
                return null;
            }

            var pathViewJson = PathHelper.combine(scope.manifest.absolute, scope.manifest.dev.views, view.get().path);
            var pathBlueprintJson = PathHelper.combine(scope.manifest.absolute, scope.manifest.dev.views, view.get().blueprint);

            provider = new DebugProvider(view.get().name, pathViewJson, pathBlueprintJson);
        }




        try {
            var n = new NodeContext(viewController, provider);
            nodeContexts.put(key, n);
            return n;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private NodeContext node;
    private String nodeName;

    @Override
    public String getName() {
        return nodeName;
    }

    @Override
    public String getId() {
        return nodeName;
    }

    @Override
    public boolean toggle() {
        var s = super.toggle();

        if(s && !getScreen().windows.containsKey(((String)data.get(VIEW_NAME)))){
            var provider = ((NodeProvider)data.get(PROVIDER));
            var name = (String)data.get(VIEW_NAME);

            var viewController = getScreen().getViewContext().viewControllers.stream()
                    .filter(p -> p.name.equals(name))
                    .findFirst();


            try {
                var n = new NodeContext(viewController, provider);
                this.node = n;
                this.nodeName = name;

                nodeContexts.put(name, n);
                n.init();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return s;
    }

    @Override
    public void render() {
        if(nextCurrentNode != null){
            currentNode = nextCurrentNode;
            nextCurrentNode = null;
            getScreen().restart();
        }

        var xOne = ImGui.getMainViewport().getSizeX() / 100.0;
        var yOne = ImGui.getMainViewport().getSizeY() / 100.0;

        ImGui.setNextWindowSize((int)(xOne * 20), (int)(yOne * 60), ImGuiCond.Once);
        ImGui.pushID(nodeName);
        if (ImGui.begin(getName(), isShow, ImGuiWindowFlags.NoCollapse)){
            var v = new ImBoolean(currentNode.equals(nodeName));
            if(ImGui.checkbox("active", v)){
                nextCurrentNode = nodeName;
            }

            if(ImGui.button("remove")){
                getScreen().removeView(nodeName);
                nodeContexts.remove(nodeName);
                if(currentNode.equals(nodeName)){
                    currentNode = "empty";
                }
                isShow = new ImBoolean(false);
            }
            node.render.render();
            ImGui.end();
        }

        ImGui.popID();
    }

    @Override
    public void init() {
        for (var node: nodeContexts.entrySet()
        ) {

            if(node.getKey().equals(currentNode)){

                if(node.getValue().viewController.isPresent()){
                    getScreen().getViewContext().jsRuntime.putMember("back_nodeContext", node.getValue());
                    try {
                        getScreen().getViewContext().jsRuntime
                                .eval("module.exports.core.controllers[" + node.getValue().viewController.get().index + "].initFunc(back_nodeContext)");
                    } catch (ScriptException e) {
                        throw new RuntimeException(e);
                    }
                }

                var scope = new ViewScope();
                scope.width = getScreen().width;
                scope.height = getScreen().height;
                scope.viewsInfo = getScreen().viewsSettings;
                scope.manifest = getScreen().manifest;
                scope.viewContext = getScreen().getViewContext();
                var view = node.getValue().startProcess(getScreen().debugShow, new ProcessService.ProcessData(), scope);
                RenderEngine.Run(view, getScreen()::pAddDrawable);
                if(node.getValue().viewController.isPresent()){
                    try {
                        getScreen().initWrappers(node.getValue().viewController.get().index, node.getValue().last_wrappers);
                    } catch (ScriptException e) {
                        GlobalContext.LOGGER.error(e.getMessage());
                    }
                }

            }
        }
    }
}
