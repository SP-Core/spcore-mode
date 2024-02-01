package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;
import spcore.appapi.Terminal;

import java.util.HashMap;

public abstract class BaseCommand {

    protected Terminal terminal;
    public void SetTerminal(Terminal terminal){
        this.terminal = terminal;
    }

    public abstract boolean Invoke(SelectionManager manager, HashMap<String, String> args);

    public abstract String GetDescription();

    public boolean OnKeyDown(String page){
        return false;
    }
}
