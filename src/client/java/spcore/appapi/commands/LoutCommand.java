package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;
import spcore.api.AuthContext;

import java.util.HashMap;

public class LoutCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        AuthContext.Logout();
        terminal.Exit();
        return false;
    }

    @Override
    public String GetDescription() {
        return "выход из аккаунта";
    }
}
