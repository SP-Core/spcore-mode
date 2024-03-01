package spcore.fabric.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.models.SpCoreInfo;

import java.util.Objects;

public class NotFoundViewScreen extends Screen {
    public NotFoundViewScreen(SpCoreInfo app) {
        super(Text.of(getTitleFromAppInfo(app)));


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    protected void init() {
        this.initWidgets();
        int var10004 = 40;
        int var10005 = this.width;
        Objects.requireNonNull(this.textRenderer);
        this.addDrawableChild(new TextWidget(0, var10004, var10005, 9, this.title, this.textRenderer));
    }

    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.of("Понял"), (button) -> {
            MinecraftClient.getInstance().setScreen(null);
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private static String getTitleFromAppInfo(SpCoreInfo app){
        var builder = new StringBuilder();
        builder.append("Приложение не имеет визуального интерфейса");
        if(app.manifest.lifetime.equals("background")){
            builder.append(", но имеет фоновые задачи");
        }
        return builder.toString();
    }
}
