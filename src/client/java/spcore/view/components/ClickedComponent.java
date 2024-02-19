//package spcore.view.components;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.Element;
//import net.minecraft.client.gui.ScreenRect;
//import net.minecraft.client.gui.Selectable;
//import net.minecraft.client.gui.navigation.GuiNavigation;
//import net.minecraft.client.gui.navigation.GuiNavigationPath;
//import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
//import net.minecraft.client.sound.PositionedSoundInstance;
//import net.minecraft.client.sound.SoundManager;
//import net.minecraft.sound.SoundEvents;
//import org.jetbrains.annotations.Nullable;
//import org.joml.Vector2i;
//import spcore.view.Component;
//import spcore.view.ComponentEvents;
//import spcore.view.ViewComponent;
//import spcore.view.render.RenderContext;
//import spcore.view.tooltip.ViewTooltip;
//
//import java.util.List;
//
//public abstract class ClickedComponent extends ViewComponent implements Selectable, Element {
//    protected boolean active;
//    private boolean focused;
//    private boolean hovered;
//    private long lastHoveredTime;
//    private boolean wasHovered;
//    protected List<ViewTooltip> tooltips;
//    protected ComponentEvents events;
//    private int navigationOrder;
//
//    protected ClickedComponent(Vector2i position, Vector2i size) {
//        super(position, size);
//        events = new ComponentEvents();
//    }
//
//    @Override
//    protected void internalConfigure(RenderContext render) {
//        super.internalConfigure(render);
//        tooltips = children.values()
//                .stream()
//                .filter(ViewTooltip.class::isInstance)
//                .map(ViewTooltip.class::cast).toList();
//    }
//
//    @Override
//    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {
//        super.internalRender(context, mouseX, mouseY, delta);
//        this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();
//
//        for (var tooltip: tooltips
//        ) {
//            tooltip.render(context, mouseX, mouseY, delta);
//        }
//    }
//
//    @Override
//    public SelectionType getType(){
//        if (this.isFocused()) {
//            return SelectionType.FOCUSED;
//        } else {
//            return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
//        }
//    }
//
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (this.active && this.visible) {
//            if (this.isValidClickButton(button)) {
//                boolean bl = this.clicked(mouseX, mouseY);
//                if (bl) {
//                    this.playDownSound(MinecraftClient.getInstance().getSoundManager());
//                    events.click(mouseX, mouseY);
//                    return true;
//                }
//            }
//
//            return false;
//        } else {
//            return false;
//        }
//    }
//
//    protected boolean isValidClickButton(int button) {
//        return button == 0;
//    }
//
//    protected boolean clicked(double mouseX, double mouseY) {
//        return this.active && this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.getWidth()) && mouseY < (double)(this.getY() + this.getHeight());
//    }
//
//    public void playDownSound(SoundManager soundManager) {
//        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
//    }
//
//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int button) {
//        if (this.isValidClickButton(button)) {
//            events.release(mouseX, mouseY);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//        return false;
//    }
//
//    @Override
//    public boolean isMouseOver(double mouseX, double mouseY) {
//        return this.active && this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.getWidth()) && mouseY < (double)(this.getY() + this.getHeight());
//    }
//
//
//    @Override
//    public void appendNarrations(NarrationMessageBuilder builder) {
//        this.appendClickableNarrations(builder);
//        for (var tooltip: tooltips
//        ) {
//            tooltip.tooltip.appendNarrations(builder);
//        }
//
//    }
//
//    @Nullable
//    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
//        if (this.active && this.visible) {
//            return !this.isFocused() ? GuiNavigationPath.of(this) : null;
//        } else {
//            return null;
//        }
//    }
//
//    protected abstract void appendClickableNarrations(NarrationMessageBuilder builder);
//
//    public void setFocused(boolean focused) {
//        this.focused = focused;
//    }
//
//    public boolean isSelected() {
//        return this.isHovered() || this.isFocused();
//    }
//
//    public boolean isNarratable() {
//        return this.visible && this.active;
//    }
//
//
//    public int getNavigationOrder() {
//        return this.navigationOrder;
//    }
//    public void setNavigationOrder(int navigationOrder) {
//        this.navigationOrder = navigationOrder;
//    }
//
//    @Override
//    public ScreenRect getNavigationFocus() {
//        return super.getNavigationFocus();
//    }
//
//    public boolean isHovered() {
//        return hovered;
//    }
//
//    public void setHovered(boolean hovered) {
//        this.hovered = hovered;
//    }
//
//    public long getLastHoveredTime() {
//        return lastHoveredTime;
//    }
//
//    public void setLastHoveredTime(long lastHoveredTime) {
//        this.lastHoveredTime = lastHoveredTime;
//    }
//
//    public boolean isWasHovered() {
//        return wasHovered;
//    }
//
//    public void setWasHovered(boolean wasHovered) {
//        this.wasHovered = wasHovered;
//    }
//
//    public boolean isFocused() {
//        return this.focused;
//    }
//}
