package spcore.spnet;

import spcore.GlobalContext;
import spcore.spnet.packets.inputs.AbstractInputPacket;
import spcore.spnet.packets.inputs.InputPacketType;
import spcore.spnet.packets.inputs.InputPacketsMap;
import spcore.spnet.packets.outputs.AbstractOutputPacket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpNetOutputThread extends Thread{
    private final SpNetConnectionClient inputs;
    private final BlockingQueue<AbstractOutputPacket> outputPackets = new LinkedBlockingQueue<>();

    public SpNetOutputThread(SpNetConnectionClient inputs) {
        this.inputs = inputs;
    }

    public boolean addPacket(AbstractOutputPacket packet){
        if(outputPackets.stream().anyMatch(p -> p.GetHash() == packet.GetHash())){
            return false;
        }
        outputPackets.add(packet);
        return true;
    }

    @Override
    public void run() {
        try {
            while (inputs.connect){
                var packet = outputPackets.take();
                packet.Write(inputs.getOutputStream());

            }
        } catch (Exception e) {
            GlobalContext.LOGGER.error("Failed to process packet from server", e);
        }
    }

}
