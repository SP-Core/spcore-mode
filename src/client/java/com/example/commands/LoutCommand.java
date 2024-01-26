package com.example.commands;

import com.example.commands.commandEngine.CommandEngine;
import com.example.spcore.SpCoreApiContext;
import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class LoutCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        SpCoreApiContext.IsAuthorized = false;
        SpCoreApiContext.Code = null;
        manager.selectAll();
        manager.delete(-1);
        manager.insert("Вы вышли из аккаунта");
        return false;
    }

    @Override
    public String GetDescription() {
        return "выход из аккаунта";
    }
}
