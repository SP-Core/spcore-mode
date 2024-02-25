//package spcore.fabric.screens.views;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.text.Text;
//import spcore.GlobalContext;
//import spcore.engine.ViewContext;
//import spcore.fabric.screens.WrapperedScreen;
//import spcore.imgui.ImGuiImpl;
//import spcore.imgui.nodes.NodeContext;
//import spcore.imgui.nodes.models.Link;
//import spcore.imgui.nodes.models.Node;
//import spcore.imgui.nodes.providers.ProductionProvider;
//import spcore.view.render.RenderEngine;
//
//import javax.script.ScriptException;
//import java.io.IOException;
//import java.util.List;
//
//public class ViewScreen extends WrapperedScreen {
//
//    public final NodeContext nodeContext;
//    public ViewScreen(ViewContext viewContext, String viewName, List<Node> nodes, List<Link> links, int lastId) {
//        super(viewContext, Text.of(viewName));
//        NodeContext nc;
//        try {
//            var bluep = "studio-views/" + viewName + ".blueprint.json";
//            nc = new NodeContext(new ProductionProvider(viewName, nodes, links, lastId), bluep, this);
//        } catch (IOException e) {
//            GlobalContext.LOGGER.error(e.getMessage());
//            MinecraftClient.getInstance().setScreen(null);
//            nc = null;
//        }
//
//        this.nodeContext = nc;
//    }
//
//    @Override
//    protected void init(){
//        super.init();
//        nodeContext.init();
//
//        var view =  nodeContext.startProcess(false);
//        RenderEngine.Run(view, this::addDrawable);
//
//        try {
//            super.initWrappers(nodeContext.last_wrappers);
//        } catch (ScriptException e) {
//            GlobalContext.LOGGER.error(e.getMessage());
//        }
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        try {
//            super.renderWrappers();
//        } catch (ScriptException e) {
//            GlobalContext.LOGGER.error(e.getMessage());
//        }
//
//        super.render(context, mouseX, mouseY, delta);
//    }
//
//}
