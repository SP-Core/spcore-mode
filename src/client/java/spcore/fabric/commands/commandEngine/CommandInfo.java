package spcore.fabric.commands.commandEngine;

import java.util.HashMap;

public class CommandInfo {
    public final String Target;
    public final HashMap<String, String> Args;

    public CommandInfo(String target, HashMap<String, String> args){
        Target = target;
        Args = args;
    }
}
