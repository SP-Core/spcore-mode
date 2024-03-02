package spcore.spnet.packets.outputs;

import spcore.spnet.models.ResourceId;
import spcore.spnet.packets.inputs.SoundPacketType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class SoundOutputPacket extends AbstractOutputPacket{

    public final SoundPacketType soundPacketType;
    public final ResourceId id;
    public SoundOutputPacket(SoundPacketType soundPacketType, ResourceId id) {
        super(OutputPacketType.Sound);
        this.soundPacketType = soundPacketType;
        this.id = id;
    }

    @Override
    protected void PacketWrite(DataOutputStream writer) throws IOException {
        writer.writeInt(s(soundPacketType.value));
        writer.writeInt(s(id.Value));
    }

    @Override
    public int GetHash() {
        return Objects.hash(soundPacketType.value, id.Value, id.Type.value);
    }
}
