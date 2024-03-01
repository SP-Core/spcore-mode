package spcore.fabric.sounds.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketAddress;

public interface ClientSpNetSocket {
    void open(String host, int port) throws Exception;

    DataInputStream getInputStream();
    DataOutputStream getOutputStream();

    void close();

    boolean isClosed();
}
