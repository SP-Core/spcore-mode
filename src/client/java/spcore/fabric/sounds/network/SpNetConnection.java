package spcore.fabric.sounds.network;

import spcore.GlobalContext;
import spcore.fabric.sounds.managers.SoundClient;
import spcore.fabric.sounds.models.SoundPacket;
import spcore.fabric.sounds.utils.SoundExceptionHandler;

import java.net.InetAddress;

public class SpNetConnection extends Thread{
    private final SoundClient client;
    private final String host;
    private final int port;
    private final ClientSpNetSocket socket;
    private final InetAddress address;
    private boolean running;
    private boolean connected;
    private long lastKeepAlive;
    public SpNetConnection(SoundClient client, String host, int port) throws Exception {
        this.client = client;
        this.host = host;
        this.port = port;
        this.address = InetAddress.getByName(host);
        this.socket = new ClientSpNetSocketImpl();
        this.lastKeepAlive = -1;
        this.running = true;
        setDaemon(true);
        setName("SpNetConnectionThread");
        setUncaughtExceptionHandler(new SoundExceptionHandler());
        this.socket.open(host, port);
    }

    public InetAddress getAddress() {
        return address;
    }

    public ClientSpNetSocket getSocket() {
        return socket;
    }

    public boolean isInitialized() {
        return connected;
    }

    @Override
    public void run() {
        try {
            var output = socket.getOutputStream();
            var input = socket.getInputStream();
            output.writeInt(4444);
            while (running) {

                var packet = SoundPacket.read(input);
                client.processSoundPacket(packet);
            }
        } catch (Exception e) {
            if (running) {
                GlobalContext.LOGGER.error("Failed to process packet from server", e);
            }
        }
    }

    public void close() {
        GlobalContext.LOGGER.info("Disconnecting voicechat");
        running = false;

        socket.close();
    }

    public boolean isConnected() {
        return running && !socket.isClosed();
    }

    public void checkTimeout() {
        if (lastKeepAlive >= 0 && System.currentTimeMillis() - lastKeepAlive > 10 * 10L) {
            GlobalContext.LOGGER.info("Connection timeout");
        }
    }


}
