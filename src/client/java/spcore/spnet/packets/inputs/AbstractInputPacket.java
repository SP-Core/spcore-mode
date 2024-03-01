package spcore.spnet.packets.inputs;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class AbstractInputPacket {
    public final InputPacketType PacketType;

    protected AbstractInputPacket(InputPacketType packetType)
    {
        PacketType = packetType;
    }

    public abstract void PacketRead(DataInputStream reader) throws IOException;

    public static int s(int intvalue)
    {
        int byte0 = ((intvalue >> 24) & 0xFF);
        int byte1 = ((intvalue >> 16) & 0xFF);
        int byte2 = ((intvalue >> 8) & 0xFF);
        int byte3 = (intvalue & 0xFF);
        int swappedvalue = (byte3 << 24) + (byte2 << 16) + (byte1 << 8) + (byte0);

        return swappedvalue;
    }
}
