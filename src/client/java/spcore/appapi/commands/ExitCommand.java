package spcore.appapi.commands;

import net.minecraft.client.util.SelectionManager;
import spcore.fabric.sounds.managers.SoundClient;
import spcore.fabric.sounds.models.ConnectionInfo;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.world.WorldThread;

import java.util.HashMap;

public class ExitCommand extends BaseCommand{
    private static SpNetConnectionClient client;
    private static WorldThread world;
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        try {
            if(client != null){
                client.close();
            }
            client = new SpNetConnectionClient("127.0.0.1", 8888);
            client.Connect();
            world = new WorldThread(client);
            world.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        terminal.Exit();
        return false;
    }

    @Override
    public String GetDescription() {
        return "выход из терминала";
    }
}
