package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class ExitCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        terminal.Exit();
        return false;
    }

    @Override
    public String GetDescription() {
        return "выход из терминала";
    }
}
