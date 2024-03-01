package spcore.fabric.sounds.models;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import spcore.fabric.sounds.utils.Utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class SoundPacket {
    public static final byte WHISPER_MASK = 0b1;
    public static final byte HAS_CATEGORY_MASK = 0b10;

    protected int channelId;
    protected int sender;
    protected byte[] data;
    protected int index;
    protected String name;
    protected Vec3d position;
    protected float distance;
//    public SoundPacket(long channelId, long sender, byte[] data, long index, Vec3d position, float distance) {
//        this.channelId = channelId;
//        this.sender = sender;
//        this.data = data;
//        this.index = index;
//        this.position = position;
//        this.distance = distance;
//    }

    public long getChannelId() {
        return channelId;
    }

    public long getSender() {
        return sender;
    }

    public byte[] getData() {
        return data;
    }

    public long getIndex() {
        return index;
    }

    public Vec3d getLocation() {
        return position;
    }

    public float getDistance() {
        return distance;
    }


    protected boolean hasFlag(byte data, byte mask) {
        return (data & mask) != 0b0;
    }

    protected byte setFlag(byte data, byte mask) {
        return (byte) (data | mask);
    }


    public static SoundPacket read(DataInputStream buf) throws IOException {
        var packet = new SoundPacket();
        packet.channelId = swapint(buf.readInt());
        packet.sender = swapint(buf.readInt());
        packet.position = new Vec3d(swapint(buf.readInt()), swapint(buf.readInt()), swapint(buf.readInt()));
        var len = swapint(buf.readInt());
        packet.data = buf.readNBytes(len);
        packet.index = swapint(buf.readInt());
        packet.distance = swapint(buf.readInt());
        return packet;
    }

    private static int swapint(int intvalue)
    {
        int byte0 = ((intvalue >> 24) & 0xFF);
        int byte1 = ((intvalue >> 16) & 0xFF);
        int byte2 = ((intvalue >> 8) & 0xFF);
        int byte3 = (intvalue & 0xFF);
        int swappedvalue = (byte3 << 24) + (byte2 << 16) + (byte1 << 8) + (byte0);

        return swappedvalue;
    }
}
