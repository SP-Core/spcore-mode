package spcore.spnet;

import net.minecraft.util.math.Vec3d;
import spcore.GlobalContext;
import spcore.spnet.models.ResourceId;
import spcore.spnet.models.ResourcePoint;
import spcore.spnet.models.ResourceType;
import spcore.spnet.packets.inputs.SoundPacketType;
import spcore.spnet.packets.outputs.ResourceModifier;
import spcore.spnet.packets.outputs.ResourceOutputPacket;
import spcore.spnet.packets.outputs.SoundOutputPacket;
import spcore.spnet.world.WorldThread;

public class SpNetClient {
    private SpNetConnectionClient client;
    private WorldThread world;
    private boolean start;

    public void start(){
        try {
            start = true;
            if(client != null){
                client.close();
            }
            client = new SpNetConnectionClient("127.0.0.1", 8888);
            client.Connect();
            world = new WorldThread(client);
            world.start();
        } catch (Exception e) {
            GlobalContext.LOGGER.error(e.getMessage());
            start = false;
        }
    }

    public void stop() throws Exception {
        if(client != null){
            client.close();
        }
        start = false;
    }

    public void startSound(int id){
        if(!start){
            return;
        }

        client.getOutputThread()
                .addPacket(new SoundOutputPacket(SoundPacketType.Start, new ResourceId(id, ResourceType.Sound)));
    }

    public void endSound(int id){
        if(!start){
            return;
        }

        client.getOutputThread()
                .addPacket(new SoundOutputPacket(SoundPacketType.End, new ResourceId(id, ResourceType.Sound)));
    }

    public void setResourcePosition(ResourceId id, Vec3d pos, ResourceModifier modifier){
        if(!start){
            return;
        }

        var curPos = world.getResourcePos(id);
        if(pos == curPos){
            return;
        }
        client.getOutputThread()
                .addPacket(new ResourceOutputPacket(new ResourcePoint(id, pos, modifier)));
    }

    private static SpNetClient instance;
    public static SpNetClient getInstance(){
        return instance;
    }


    public static void registration(){
        instance = new SpNetClient();
    }
}
