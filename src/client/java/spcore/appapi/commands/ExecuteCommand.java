package spcore.appapi.commands;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.util.SelectionManager;
import spcore.GlobalContext;
import spcore.api.models.PriceList;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.configuration.PATHS;
import spcore.appapi.helpers.PathHelper;
import spcore.appapi.models.SpCoreInfo;
import spcore.engine.AppEngine;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ExecuteCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {

        if(args.size() == 0){
            manager.insert("не указано запускаемое приложение");
            return false;
        }

        var pathKey = args.get("1");
        var path = PATHS.get(pathKey);
        if(path ==  null){
            manager.insert("неизвестное приложение");
            return false;
        }

        var spCorePath = PathHelper.combine(path, "spcore.json");
        if(!Files.exists(Path.of(spCorePath))){
            manager.insert("файл spcore.json не найден");
            return false;
        }

        String content;
        try {
            content = Files.readString(Path.of(spCorePath));
        } catch (IOException e) {
            manager.insert("ошибка чтения файла");
            return false;
        }

        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper(gson);
        var spCoreInfo = mapper.readValue(content, SpCoreInfo.class);
        spCoreInfo.absolute = path;
        KnowApplicationManager.resolveAndRun(spCoreInfo);
        return false;
    }

    @Override
    public String GetDescription() {
        return "execute command";
    }
}
