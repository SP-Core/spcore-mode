package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class SignCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        var size = args.size();
        if(size == 0){
            manager.insert("Укажите название первым параметром");
            return false;
        }

        if(size == 1){
            var title = args.get("1");
            try{
                terminal.Sign(title);
                manager.insert("Книга подписана");
            }
            catch (Exception e){
                manager.insert("Нельзя подписать");
            }

        }
        return false;
    }

    @Override
    public String GetDescription() {
        return "Подписать книгу";
    }
}
