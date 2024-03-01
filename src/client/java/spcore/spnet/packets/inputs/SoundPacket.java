package spcore.spnet.packets.inputs;

import spcore.spnet.models.ResourceId;
import spcore.spnet.models.ResourceType;

import java.io.DataInputStream;
import java.io.IOException;

public class SoundPacket extends AbstractInputPacket{
    protected SoundPacket() {
        super(InputPacketType.Sound);
    }

    public SoundPacketType soundPacketType;
    public ResourceId resourceId;

    @Override
    public void PacketRead(DataInputStream reader) throws IOException {
        this.soundPacketType = SoundPacketType.fromInteger(s(reader.readInt()));
        this.resourceId = new ResourceId(s(reader.readInt()), ResourceType.Sound);
    }
}
