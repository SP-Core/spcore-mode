package spcore.spnet.packets.outputs;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractOutputPacket {
    public final OutputPacketType PacketType;

    protected AbstractOutputPacket(OutputPacketType packetType) {
        PacketType = packetType;
    }

    protected abstract void PacketWrite(DataOutputStream writer) throws IOException;

    public void Write(DataOutputStream writer) throws IOException {
        writer.writeInt(s(PacketType.value));
        PacketWrite(writer);
    }

    public abstract int GetHash();

    public static int s(int swappedvalue)
    {
        int byte0 = ((swappedvalue >> 24) & 0xFF);
        int byte1 = ((swappedvalue >> 16) & 0xFF);
        int byte2 = ((swappedvalue >> 8) & 0xFF);
        int byte3 = (swappedvalue & 0xFF);
        int intValue = (byte3 << 24) + (byte2 << 16) + (byte1 << 8) + (byte0);

        return intValue;
    }
}
