package spcore.spnet.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import spcore.GlobalContext;
import spcore.fabric.handlers.WrittenBookHandler;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.models.ResourceId;
import spcore.spnet.packets.inputs.*;
import spcore.spnet.packets.outputs.UserPositionOutputPacket;
import spcore.spnet.resources.SpNetResourceStorage;

import java.util.HashMap;

public class WorldThread extends Thread{
    private final SpNetConnectionClient client;
    private MinecraftClient mc;

    private Vec3d lastPosition;

    private final HashMap<ResourceId, Vec3d> resourcesPositions = new HashMap<>();
    private final HashMap<ResourceId, SoundChannel> soundChannel = new HashMap<>();
    private final ResourcesResolver resourcesResolver;
    public WorldThread(SpNetConnectionClient client) {
        this.client = client;
        this.mc = MinecraftClient.getInstance();
        this.resourcesResolver = new ResourcesResolver(client);
    }

    public Vec3d getResourcePos(ResourceId id){
        if(resourcesPositions.containsKey(id)){
            return resourcesPositions.get(id);
        }
        return new Vec3d(0, 0, 0);
    }
    @Override
    public void run() {
        this.resourcesResolver.start();
        while (client.connect){
            try {
                positionUpdate();
                packagesUpdate();
                WrittenBookHandler.handle(true);
                Thread.sleep(100);
            }
            catch (Exception e){
                GlobalContext.LOGGER.error("Failed to process packet from server", e);
            }
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
        }
    }

    public void packagesUpdate(){
        while (client.getInputThread().inputPackets.size() != 0){
            var packet = client.getInputThread().inputPackets.remove();
            if(packet instanceof ResourcePacket resourcePacket){
                resourcesPositions.put(resourcePacket.Point.Id, resourcePacket.Point.Position);
                resourcesResolver.addResource(resourcePacket.Point.Id);
            }
            else if(packet instanceof SoundPacket soundPacket){
                if(soundPacket.soundPacketType == SoundPacketType.Start && !soundChannel.containsKey(soundPacket.resourceId)){
                    var channel = new SoundChannel(client, resourcesResolver, soundPacket.resourceId, this);
                    channel.start();
                    soundChannel.put(soundPacket.resourceId, channel);
                }
                else if(soundPacket.soundPacketType == SoundPacketType.End && soundChannel.containsKey(soundPacket.resourceId)){
                    var channel = soundChannel.get(soundPacket.resourceId);
                    channel.Stop();
                    soundChannel.remove(soundPacket.resourceId);
                }
            }

        }

    }

}
