package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;
import spcore.appapi.configuration.PATHS;

import java.util.HashMap;

public class PathCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {

        var builder = new StringBuilder();
        for (var p: args.values()
             ) {
            builder.append(p);
        }

        PATHS.setPath(builder.toString());
        manager.insert("Путь добавлен");
        return false;
    }

    @Override
    public String GetDescription() {
        return "paths";
    }
}
