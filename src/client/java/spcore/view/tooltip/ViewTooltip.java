//package spcore.view.tooltip;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.tooltip.Tooltip;
//import net.minecraft.client.gui.tooltip.TooltipPositioner;
//import net.minecraft.text.Text;
//import net.minecraft.util.Util;
//import org.jetbrains.annotations.Nullable;
//import spcore.view.ViewComponent;
//import spcore.view.render.RenderContext;
//import spcore.view.render.Renderable;
//
//public class ViewTooltip extends Renderable {
//    public final Tooltip tooltip;
//    private TooltipOptions options;
//
//    private ViewTooltip(Tooltip tooltip) {
//        super(position);
//        this.tooltip = tooltip;
//        this.options = new TooltipOptions();
//    }
//
//    public static ViewTooltip of(Text content, @Nullable Text narration) {
//        return new ViewTooltip(Tooltip.of(content, narration));
//    }
//
//    public static ViewTooltip of(Text content) {
//        return new ViewTooltip(Tooltip.of(content, content));
//    }
//
//
//    @Override
//    protected void internalConfigure(RenderContext render) {
//        options = render.options.getValue(render.options.tooltip, options);
//    }
//
//    @Override
//    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {
//
//        if(super.getParent() instanceof ViewComponent viewComponent){
//            boolean bl = viewComponent.isHovered() || viewComponent.isFocused() && MinecraftClient.getInstance().getNavigationType().isKeyboard();
//            if (bl != viewComponent.isWasHovered()) {
//                if (bl) {
//                    viewComponent.setLastHoveredTime(Util.getMeasuringTimeMs());
//                }
//
//                viewComponent.setWasHovered(bl);
//            }
//
//            if (bl && Util.getMeasuringTimeMs() - viewComponent.getLastHoveredTime() > (long)this.options.getTooltipDelay()) {
//                Screen screen = MinecraftClient.getInstance().currentScreen;
//                if (screen != null) {
//                    screen.setTooltip(this.tooltip, this.getTooltipPositioner(viewComponent), viewComponent.isFocused());
//                }
//            }
//        }
//
//
//    }
//
//    protected TooltipPositioner getTooltipPositioner(ViewComponent viewComponent) {
//        return (TooltipPositioner)(!viewComponent.isHovered() && viewComponent.isFocused() &&
//                MinecraftClient.getInstance()
//                        .getNavigationType().isKeyboard() ?
//                new CoreFocusedTooltipPositioner(viewComponent) :
//                new ComponentTooltipPositioner(viewComponent));
//    }
//
//
//
//}
