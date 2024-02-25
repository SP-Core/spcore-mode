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
import spcore.imgui.nodes.models.ViewScope;
import spcore.imgui.nodes.providers.DebugProvider;
import spcore.view.render.RenderEngine;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GuiNodesWindow extends AbstractWindow{

    public static final String VIEW = "view";

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
        if(s && !getScreen().windows.containsKey(((ViewsInfo.View)data.get(VIEW)).name)){
            var view = ((ViewsInfo.View)data.get(VIEW));

            var pathViewJson = PathHelper.combine(getScreen().manifest.absolute, getScreen().manifest.dev.views, view.path);
            var pathBlueprintJson = PathHelper.combine(getScreen().manifest.absolute, getScreen().manifest.dev.views, view.blueprint);

            var provider = new DebugProvider(view.name, pathViewJson);
            var viewController = getScreen().getViewContext().viewControllers.stream()
                    .filter(p -> p.name.equals(view.name))
                    .findFirst();


            try {
                var n = new NodeContext(viewController, provider, pathBlueprintJson);
                this.node = n;
                this.nodeName = view.name;
                nodeContexts.put(view.name, n);
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
    }

    @Override
    public void init() {
        for (var node: nodeContexts.entrySet()
        ) {
            if(node.getKey().equals(currentNode)){
                node.getValue().init();
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

                var view = node.getValue().startProcess(getScreen().debugShow, scope);
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
