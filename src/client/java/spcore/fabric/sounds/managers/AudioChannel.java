package spcore.fabric.sounds.managers;

import com.google.common.primitives.Shorts;
import io.netty.buffer.Unpooled;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import net.minecraft.client.MinecraftClient;
import spcore.GlobalContext;
import spcore.fabric.sounds.codec.CoreOpusDecoder;
import spcore.fabric.sounds.codec.OpusManager;
import spcore.fabric.sounds.models.SoundPacket;
import spcore.fabric.sounds.utils.AudioPacketBuffer;
import spcore.fabric.sounds.utils.FreecamUtil;
import spcore.fabric.sounds.utils.SoundExceptionHandler;
import spcore.fabric.sounds.utils.Utils;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class AudioChannel extends Thread{

    private SoundClient client;
    private final MinecraftClient minecraft;
    private final long id;
    private final BlockingQueue<SoundPacket> queue;
    private final AudioPacketBuffer packetBuffer;
    private long lastPacketTime;
    private AudioManager speaker;
    private boolean stopped;
    private final CoreOpusDecoder decoder;
    private long lastSequenceNumber;
    private long lostPackets;

    public AudioChannel(SoundClient client, long id) {
        this.client = client;
        this.id = id;
        this.queue = new LinkedBlockingQueue<>();
        this.packetBuffer = new AudioPacketBuffer(3);
        this.lastPacketTime = System.currentTimeMillis();
        this.stopped = false;
        this.decoder = OpusManager.createDecoder();
        this.lastSequenceNumber = -1L;
        this.minecraft = MinecraftClient.getInstance();
        setDaemon(true);
        setName("AudioChannelThread-" + id);
        setUncaughtExceptionHandler(new SoundExceptionHandler());
        GlobalContext.LOGGER.info("Creating audio channel for {}", id);
    }

    public boolean canKill() {
        return System.currentTimeMillis() - lastPacketTime > 30_000L;
    }


    public void closeAndKill() {
        GlobalContext.LOGGER.info("Closing audio channel for {}", id);
        stopped = true;
        queue.clear();
        if (Thread.currentThread() == this) {
            return;
        }
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
            GlobalContext.LOGGER.error("Interrupted while waiting for audio channel to close", e);
        }
    }


    public long getId() {
        return id;
    }

    public void addToQueue(SoundPacket p) {
        queue.add(p);
    }

    public void run() {
        try {
            if (client.getSoundManager() == null) {
                throw new IllegalStateException("Started audio channel without sound manager");
            }

            speaker = SpeakerManager.createSpeaker(client.getSoundManager(), id);

            while (!stopped) {
//                if (!ClientManager.isActive()) {
//                    closeAndKill();
//                    return;
//                }

                SoundPacket packet = packetBuffer.poll(queue);
                if (packet == null) {
                    continue;
                }
                lastPacketTime = System.currentTimeMillis();

                if (lastSequenceNumber >= 0 && packet.getIndex() <= lastSequenceNumber) {
                    continue;
                }

                if (minecraft.getLevelStorage() == null || minecraft.player == null) {
                    continue;
                }

                if (packet.getData().length == 0) {
                    lastSequenceNumber = -1L;
                    packetBuffer.clear();
                    decoder.resetState();
                    continue;
                }

                if (lastSequenceNumber >= 0) {
                    int packetsToCompensate = (int) (packet.getIndex() - (lastSequenceNumber + 1));

                    if (packetsToCompensate > 0) {
                        GlobalContext.LOGGER.debug("Compensating {}/{} packets ", packetsToCompensate >= 4 ? 0 : packetsToCompensate, packetsToCompensate);
                    }

                    if (packetsToCompensate <= 4) {
                        lostPackets += packetsToCompensate;
                        for (int i = 0; i < packetsToCompensate; i++) {
                            writeToSpeaker(packet, decoder.decode(null));
                        }
                    } else {
                        GlobalContext.LOGGER.debug("Skipping compensation for {} packets", packetsToCompensate);
                    }
                }

                lastSequenceNumber = packet.getIndex();
//                InputStream inputStream = new ByteArrayInputStream(packet.getData());

                var ff = getPcmByteArray("C:\\Users\\jackf\\Desktop\\arcoin\\fee\\mus.wav");
//                FileInputStream fileInputStream = new FileInputStream("C:\\Users\\jackf\\Desktop\\arcoin\\fee\\mus.wav");
//                InputStream bufferedIn = new BufferedInputStream(fileInputStream);
//                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
//                audioIn.skipNBytes(10000);
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioIn);
//                clip.start();
                short[] shorts = new short[ff.length/2];
                FileInputStream fileInputStream = new FileInputStream("C:\\Users\\jackf\\Desktop\\arcoin\\fee\\mus.wav");

                var f = fileInputStream.readAllBytes();

                ByteBuffer.wrap(ff).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

                short[] shorts2 = new short[ff.length/2];
                ByteBuffer.wrap(f).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts2);


                Sound.playSound("C:\\Users\\jackf\\Desktop\\arcoin\\fee\\mus.wav").join();
            }
        } catch (InterruptedException ignored) {
        } catch (Throwable e) {
            GlobalContext.LOGGER.error("Audio channel error", e);
        } finally {
            if (speaker != null) {
                speaker.close();
            }
            decoder.close();
            GlobalContext.LOGGER.info("Closed audio channel for {}", id);
        }
    }

    public static byte[] getPcmByteArray(String filename) throws UnsupportedAudioFileException, IOException {

        File inputFile = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(filename);
        InputStream bufferedIn = new BufferedInputStream(fileInputStream, fileInputStream.available());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(fileInputStream.available());

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

        return audioInputStream.readAllBytes();
    }


    private void writeToSpeaker(SoundPacket packet, short[] monoData) {
        float volume = 1;

        if (FreecamUtil.getDistanceTo(packet.getLocation()) > packet.getDistance() + 1D) {
            return;
        }
        speaker.play(monoData, volume, packet.getLocation(), "aboba", packet.getDistance());
    }

    public boolean isClosed() {
        return stopped;
    }

    public BlockingQueue<SoundPacket> getQueue() {
        return queue;
    }

    public AudioManager getSpeaker() {
        return speaker;
    }

    public AudioPacketBuffer getPacketBuffer() {
        return packetBuffer;
    }

    public long getLostPackets() {
        return lostPackets;
    }

}
