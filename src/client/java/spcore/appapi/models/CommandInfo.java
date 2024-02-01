package spcore.appapi.models;

import java.util.HashMap;

public class CommandInfo {
    public final String Target;
    public final HashMap<String, String> Args;

    public CommandInfo(String target, HashMap<String, String> args){
        Target = target;
        Args = args;
    }

    public static CommandInfo Parse(String command){
        var lines = command.split(" ");
        var target = lines[0];
        HashMap<String, String> args = new HashMap<>();
        String currentKey = null;
        int subKey = 0;
        if(lines.length > 1){
            for (var i = 1; i < lines.length; i++){
                var line = lines[i];
                if(line.startsWith("-")){
                    currentKey = line.substring(1);
                }
                else{
                    if(currentKey == null){
                        subKey++;
                        currentKey = String.valueOf(subKey);
                        args.put(currentKey, lines[i]);
                    }
                    else{
                        args.put(currentKey, lines[i]);
                    }
                    currentKey = null;
                }
            }
        }

        return new CommandInfo(target, args);

    }
}
