package spcore.spnet;

import spcore.GlobalContext;
import spcore.fabric.sounds.models.SoundPacket;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.packets.inputs.AbstractInputPacket;
import spcore.spnet.packets.inputs.InputPacketType;
import spcore.spnet.packets.inputs.InputPacketsMap;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpNetInputThread extends Thread{
    private final SpNetConnectionClient inputs;
    public final BlockingQueue<AbstractInputPacket> inputPackets = new LinkedBlockingQueue<>();

    public SpNetInputThread(SpNetConnectionClient inputs) {
        this.inputs = inputs;
    }

    @Override
    public void run() {
        try {
            while (inputs.connect){
                AbstractInputPacket packet;
                synchronized (inputs.lock)
                {
                    var packetTypeInt = AbstractInputPacket.s(inputs.getInputStream().readInt());
                    var packetType = InputPacketType.fromInteger(packetTypeInt);
                    packet = InputPacketsMap.ResolvePacket(packetType);
                    packet.PacketRead(inputs.getInputStream());
                }
                inputPackets.add(packet);
            }
        } catch (Exception e) {
            GlobalContext.LOGGER.error("Failed to process packet from server", e);
        }
    }
}
