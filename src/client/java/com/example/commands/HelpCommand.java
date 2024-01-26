package com.example.commands;

import com.example.commands.commandEngine.CommandEngine;
import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        StringBuilder message = new StringBuilder();
        var commands = CommandEngine.GetCommands();

        for (Map.Entry<String, BaseCommand> command: commands.entrySet()) {
            message.append("\n");
            message.append(command.getKey()).append(": ").append(command.getValue().GetDescription());
        }

        manager.insert(message.toString());
        return false;
    }

    @Override
    public String GetDescription() {
        return "команды";
    }
}
