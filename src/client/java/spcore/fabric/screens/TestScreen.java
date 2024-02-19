//package spcore.fabric.screens;
//
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.GridWidget;
//import net.minecraft.text.Text;
//import spcore.GlobalContext;
//import spcore.imgui.ImGuiImpl;
//import spcore.imgui.nodes.Graph;
//import spcore.imgui.nodes.NodeContext;
//import spcore.imgui.nodes.NodeEditorRender;
//import spcore.view.ViewComponent;
//import spcore.view.components.TextComponent;
//import spcore.view.layouts.GridComponent;
//import spcore.view.render.RenderContext;
//import spcore.view.render.RenderEngine;
//import spcore.view.render.Renderable;
//import spcore.view.xml.XmlConvert;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//public class TestScreen extends Screen {
//    public final Graph graph = new Graph();
//    public final NodeContext nodeContext = new NodeContext();
//    public TestScreen() {
//        super(Text.empty());
//
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//        nodeContext.init();
//        File file = new File("C:\\Users\\jackf\\Desktop\\test\\main.xml");
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            GlobalContext.LOGGER.error(e.getMessage());
//            return;
//        }
//
//        XmlConvert.variables.put("--screen-width", String.valueOf(this.width));
//        XmlConvert.variables.put("--screen-height", String.valueOf(this.height));
//
//        Renderable component = null;
//        try {
//            component = XmlConvert.deserialize(inputStream);
//        } catch (Exception e) {
//            GlobalContext.LOGGER.error(e.getMessage());
//            return;
//        }
//        RenderEngine.Run(component, this::addDrawable);
//
////        var grid = new GridComponent();
////        var settingAdder = grid.createAdder(1);
////        settingAdder.getMainPositioner().margin(10);
////        settingAdder.add(new TextComponent("ЧТОООО", textRenderer));
////
////        var view = new ViewComponent();
////        view.setBackground("#262626");
////        var line = new ViewComponent();
////        line.setBackground("#F5DEB3");
////        line.setWidth(this.width);
////        line.setHeight(this.height);
////        line.setX1(0);
////        line.setY1(-20);
////        line.setX2(100);
////        line.setY2(-40);
////        line.setRotate(50);
////        view.addChild(line);
////        view.addChild(grid);
////        view.setWidth(this.width);
////        view.setHeight(this.height);
////        RenderEngine.Run(view, this::addDrawable);
//
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        super.render(context, mouseX, mouseY, delta);
//
//        ImGuiImpl.draw(io -> {
//            nodeContext.render.render();
////            NodeEditorRender.Render(io, graph);
//        });
//    }
//
//
//}
