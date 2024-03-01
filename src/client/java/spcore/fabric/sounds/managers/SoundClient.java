package spcore.fabric.sounds.managers;

import spcore.GlobalContext;
import spcore.fabric.sounds.models.ConnectionInfo;
import spcore.fabric.sounds.models.SoundPacket;
import spcore.fabric.sounds.network.SpNetConnection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SoundClient {
    @Nullable
    private SpCoreSoundManager soundManager;
    private final Map<Long, AudioChannel> audioChannels;
    @Nullable
    private SpNetConnection connection;
    private long startTime;
    private ConnectionInfo connectionInfo;

    public SoundClient() {
        this.startTime = System.currentTimeMillis();
        try {
            reloadSoundManager();
        } catch (Exception e) {
            GlobalContext.LOGGER.error("Failed to start sound manager", e);
        }
        this.audioChannels = new HashMap<>();
    }

    public void onVoiceChatConnected(SpNetConnection connection) {

    }

    public void onVoiceChatDisconnected() {

        if (connection != null) {
            connection.close();
            connection = null;
        }
    }


    public void connect(ConnectionInfo connectionInfo) throws Exception {
        GlobalContext.LOGGER.info("Connecting to voice chat server: '{}:{}'", connectionInfo.getHost(), connectionInfo.getPort());
        connection = new SpNetConnection(this, connectionInfo.getHost(), connectionInfo.getPort());
        connection.start();
    }


    public void processSoundPacket(SoundPacket packet) {
        if (connection == null) {
            return;
        }
        synchronized (audioChannels) {
            AudioChannel sendTo = audioChannels.get(packet.getChannelId());
            if (sendTo == null) {
                try {
                    AudioChannel ch = new AudioChannel(this, packet.getChannelId());
                    ch.addToQueue(packet);
                    ch.start();
                    audioChannels.put(packet.getChannelId(), ch);
                } catch (Exception e) {
                    GlobalContext.LOGGER.error("Failed to create audio channel", e);

                }
            } else {
                sendTo.addToQueue(packet);
            }
            audioChannels.values().stream().filter(AudioChannel::canKill).forEach(AudioChannel::closeAndKill);
            audioChannels.entrySet().removeIf(entry -> entry.getValue().isClosed());

        }
    }

    public void reloadSoundManager() throws Exception {
        if (soundManager != null) {
            soundManager.close();
        }
        soundManager = new SpCoreSoundManager("");
    }

    public void reloadAudio() {
        GlobalContext.LOGGER.info("Reloading audio");

        synchronized (audioChannels) {
            GlobalContext.LOGGER.info("Clearing audio channels");
            audioChannels.forEach((uuid, audioChannel) -> audioChannel.closeAndKill());
            audioChannels.clear();
            try {
                GlobalContext.LOGGER.info("Restarting sound manager");
                reloadSoundManager();
            } catch (Exception e) {
                GlobalContext.LOGGER.error("Failed to restart sound manager", e);
            }
        }

        GlobalContext.LOGGER.info("Starting microphone thread");
    }



    @Nullable
    public SpNetConnection getConnection() {
        return connection;
    }

    @Nullable
    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    @Nullable
    public SpCoreSoundManager getSoundManager() {
        return soundManager;
    }

    public long getStartTime() {
        return startTime;
    }

    public Map<Long, AudioChannel> getAudioChannels() {
        return audioChannels;
    }


    public boolean closeAudioChannel(long id) {
        synchronized (audioChannels) {
            boolean removed = audioChannels.remove(id) != null;
            if (removed) {
                GlobalContext.LOGGER.debug("Removed audio channel of {} due to disconnection from voice chat", id);
            }
            return removed;
        }
    }

    public void close() {
        synchronized (audioChannels) {
            GlobalContext.LOGGER.info("Clearing audio channels");
            audioChannels.forEach((uuid, audioChannel) -> audioChannel.closeAndKill());
            audioChannels.clear();
        }

        if (soundManager != null) {
            soundManager.close();
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }

    }
}
