package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class SoundCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        try{
            terminal.Sign("♬sound-lab " + 111);
            manager.insert("Книга подписана");
        }
        catch (Exception e){
            manager.insert("Нельзя подписать");
        }
        return false;
    }

    @Override
    public String GetDescription() {
        return "Создает музыкальный предмет";
    }
}
