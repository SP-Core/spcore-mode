package spcore.appapi;

import spcore.appapi.commands.*;
import spcore.appapi.models.CommandInfo;

import java.util.HashMap;

public class CommandEngine {

    public final Terminal terminal;
    private static HashMap<String, BaseCommand> _commands;

    static {
        _commands = new HashMap<>();
        _commands.put("help", new HelpCommand());
        _commands.put("lout", new LoutCommand());
        _commands.put("tree", new TreeCommand());
        _commands.put("exit", new ExitCommand());
        _commands.put("sign", new SignCommand());
    }

    public CommandEngine(Terminal terminal) {
        this.terminal = terminal;
    }

    public static HashMap<String, BaseCommand> GetCommands(){
        return _commands;
    }

    public String Execute(String command){
        var commandInfo = CommandInfo.Parse(command);
        if(_commands.containsKey(commandInfo.Target)){
            var c = _commands.get(commandInfo.Target);
            c.SetTerminal(terminal);
            c.Invoke(terminal.getManager(), commandInfo.Args);
            return commandInfo.Target;
        }
        return null;
    }
}
