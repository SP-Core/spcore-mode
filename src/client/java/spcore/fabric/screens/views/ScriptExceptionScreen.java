package spcore.fabric.screens.views;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import spcore.appapi.models.SpCoreInfo;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScriptExceptionScreen extends Screen {

    private final Text mainText;
    private final List<Text> lines;
    public ScriptExceptionScreen(SpCoreInfo app, Exception e) {
        super(Text.of(""));

        e.printStackTrace();
        mainText = Text.of("У приложения " + app.manifest.name + " произошла ошибка");
        int chunkSize = 60;

        var message = e.getMessage();
        lines = new ArrayList<>();
        for (int i = 0; i < message.length(); i += chunkSize) {
            int end = Math.min(message.length(), i + chunkSize);
            lines.add(Text.of(message.substring(i, end)));
        }
    }

    protected void init() {
        this.initWidgets();
        int var10005 = this.width;
        Objects.requireNonNull(this.textRenderer);
        this.addDrawableChild(new TextWidget(0, 40, var10005, 9, mainText, this.textRenderer));
        for(var i = 0; i < lines.size(); i++){
            this.addDrawableChild(new TextWidget(0, 50 + (10 * i), var10005, 9, lines.get(i), this.textRenderer)
                    .setTextColor(11141120));
        }

    }

    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.of("Закрыть"), (button) -> {
            MinecraftClient.getInstance().setScreen((Screen) null);
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}
