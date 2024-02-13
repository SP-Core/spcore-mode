package spcore.fabric.screens.views;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import spcore.appapi.Terminal;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.engine.models.CommandMapItem;
import spcore.fabric.screens.TerminalScreen;

public class CommandMapScreen extends TerminalScreen {

    private final CommandMapItem[] mapItems;
    public CommandMapScreen(Terminal terminal, CommandMapItem[] mapItems) {
        super(terminal);
        this.mapItems = mapItems;
    }

    @Override
    protected void init() {
        super.init();
        initCommands();
    }

    private void initCommands(){
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(2, 2, 2, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);

        for (var command: mapItems
        ) {
            adder.add(ButtonWidget.builder(Text.of(command.name), (button) -> {
                showTerminal();
                sendCommand(command.command);
            }).width(150).build());
        }
        var i = this.height / 2 - (192 / 2) - 50;
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, i, 5, this.width, this.height, 0F, 0F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

}
