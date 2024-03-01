package spcore.fabric.sounds.network;

import java.net.SocketAddress;

public class RawPacketImpl implements RawPacket{
    private final byte[] data;
    private final SocketAddress socketAddress;
    private final long timestamp;

    public RawPacketImpl(byte[] data, SocketAddress socketAddress, long timestamp) {
        this.data = data;
        this.socketAddress = socketAddress;
        this.timestamp = timestamp;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}
