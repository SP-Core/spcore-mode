package spcore.fabric.sounds.network;

import java.net.SocketAddress;

public interface RawPacket {
    byte[] getData();

    long getTimestamp();

    SocketAddress getSocketAddress();
}
