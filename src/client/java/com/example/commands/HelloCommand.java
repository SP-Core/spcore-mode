package com.example.commands;

import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class HelloCommand extends BaseCommand {

    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        manager.insert("Hello world!");
        return false;
    }

    @Override
    public String GetDescription() {
        return "приветствие";
    }
}
