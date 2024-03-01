package spcore.spnet.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import spcore.GlobalContext;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.packets.inputs.AbstractInputPacket;
import spcore.spnet.packets.inputs.InputPacketType;
import spcore.spnet.packets.inputs.InputPacketsMap;
import spcore.spnet.packets.outputs.UserPositionOutputPacket;

public class WorldThread extends Thread{
    private final SpNetConnectionClient client;
    private MinecraftClient mc;

    private Vec3d lastPosition;
    public WorldThread(SpNetConnectionClient client) {
        this.client = client;
        this.mc = MinecraftClient.getInstance();
    }

    @Override
    public void run() {
        try {
            while (client.connect){
                positionUpdate();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            GlobalContext.LOGGER.error("Failed to process packet from server", e);
        }
    }

    public void positionUpdate(){

        if(mc.player == null)
            return;

        var pos = mc.player.getPos();
        if(lastPosition != null && lastPosition.equals(pos)){
            return;
        }
        var posPacket = new UserPositionOutputPacket(10, pos);
        var s = client.getOutputThread().addPacket(posPacket);
        if(s){
            lastPosition = pos;
            GlobalContext.LOGGER.info("add pos " + posPacket.Position.x + " " + posPacket.Position.y + " " + posPacket.Position.z);
        }
    }
}
