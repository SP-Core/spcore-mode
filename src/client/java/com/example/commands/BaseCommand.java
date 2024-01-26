package com.example.commands;

import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public abstract class BaseCommand {

    public abstract boolean Invoke(SelectionManager manager, HashMap<String, String> args);

    public abstract String GetDescription();

    public boolean OnKeyDown(int keyCode, int scanCode, int modifiers, String page){
        return false;
    }
}
