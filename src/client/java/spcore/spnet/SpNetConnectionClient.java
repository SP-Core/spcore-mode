package spcore.spnet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SpNetConnectionClient implements AutoCloseable{
    private final String host;
    private final int port;
    private final Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    public boolean connect;
    public final Object lock = new Object();

    private SpNetInputThread inputThread;
    private SpNetOutputThread outputThread;

    public SpNetConnectionClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    public void Connect() throws IOException {
        connect = true;
        this.socket.connect(new InetSocketAddress(InetAddress.getByName(host), port));
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        inputThread = new SpNetInputThread(this);
        inputThread.start();
        outputThread = new SpNetOutputThread(this);
        outputThread.start();
    }



    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void close() throws Exception {
        connect = false;
        socket.close();
    }

    public SpNetInputThread getInputThread() {
        return inputThread;
    }

    public SpNetOutputThread getOutputThread() {
        return outputThread;
    }
}
