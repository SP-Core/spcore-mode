package spcore.fabric.commands;

import spcore.fabric.commands.commandEngine.CommandEngine;
import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class CLSCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        manager.selectAll();
        manager.delete(-1);
        manager.insert(CommandEngine.MessageOpenTerminal());

        return  false;
    }

    @Override
    public String GetDescription() {
        return "очистить терминал";
    }
}
