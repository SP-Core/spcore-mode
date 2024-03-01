package spcore.fabric.screens;

import imgui.ImGui;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.models.KnownApplication;
import spcore.appapi.models.SpCoreInfo;

import java.util.Objects;
import java.util.function.Supplier;

public class AppResolverScreen extends Screen {

    private final SpCoreInfo info;

    public static int CurrentAppHash;
    public static boolean Resolve;
    public AppResolverScreen(SpCoreInfo info) {
        super(Text.of(createMessage(info)));
        this.info = info;
        CurrentAppHash = info.hashCode();
        Resolve = false;
    }

    private static String createMessage(SpCoreInfo info){
        if(!info.manifest.host_access.equals("ALL")){
            return "Обнаружено новое приложение!";
        }

        return "Обнаружено новое приложение, запрашивающее права администратора!";
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
        adder.add(ButtonWidget.builder(Text.of("Разрешить и запустить"), (button) -> {
            Resolve = true;
            KnowApplicationManager.resolveAndRun(info);
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));

        adder.add(ButtonWidget.builder(Text.of("Отмена"), (button) -> {
            KnowApplicationManager.notTrust(info);
            MinecraftClient.getInstance().setScreen(null);
        }).width(204).build(), 2);

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

}
