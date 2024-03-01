package spcore.fabric.sounds.network;

import spcore.GlobalContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSpNetSocketImpl implements ClientSpNetSocket{
    private final byte[] BUFFER = new byte[4096];

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    @Override
    public void open(String host, int port) throws Exception {
        this.socket = new Socket(host, port);
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
    }

    public DataInputStream getInputStream(){
        return inputStream;
    }
    public DataOutputStream getOutputStream(){
        return outputStream;
    }

    @Override
    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }
    }

    @Override
    public boolean isClosed() {
        return socket == null;
    }
}
