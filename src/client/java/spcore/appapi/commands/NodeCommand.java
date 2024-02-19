//package spcore.appapi.commands;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.util.SelectionManager;
//
//import java.util.HashMap;
//
//public class NodeCommand extends BaseCommand{
//    @Override
//    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
//        var mc = MinecraftClient.getInstance();
//        mc.setScreen(new NodeEditorScreen());
//        return false;
//    }
//
//    @Override
//    public String GetDescription() {
//        return "Node editor";
//    }
//}
