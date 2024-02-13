package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;
import spcore.appapi.background.BackgroundJobManager;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.configuration.PATHS;

import java.util.HashMap;

public class UpCommand extends BaseCommand {

    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        if(args.size() == 0){
            manager.insert("не указано название приложения");
            return false;
        }

        var pathKey = args.get("1");
        var path = PATHS.get(pathKey);
        if(path ==  null){
            manager.insert("неизвестное приложение");
            return false;
        }

        var app = KnowApplicationManager
                .getApplications().stream()
                .filter(p -> p.Manifest.absolute.equals(path))
                .findFirst();
        if(app.isEmpty()){
            manager.insert("неизвестное приложение");
            return false;
        }

        BackgroundJobManager.getInstance().remove(app.get().Manifest);
        KnowApplicationManager.resolveAndRun(app.get().Manifest);

        return false;
    }

    @Override
    public String GetDescription() {
        return "обновляет приложение";
    }
}
