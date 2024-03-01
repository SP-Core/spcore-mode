package spcore.fabric.sounds.utils;

import spcore.fabric.sounds.models.SoundPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AudioPacketBuffer {

    private final int packetThreshold;
    @Nullable
    private List<SoundPacket> packetBuffer;
    private long lastSequenceNumber = -1;
    private boolean isFlushingBuffer;

    public AudioPacketBuffer(int packetThreshold) {
        this.packetThreshold = packetThreshold;
        if (packetThreshold > 0) {
            this.packetBuffer = new ArrayList<>();
        }
    }

    @Nullable
    public SoundPacket poll(BlockingQueue<SoundPacket> queue) throws InterruptedException {
        if (packetThreshold <= 0) {
            return queue.poll(10, TimeUnit.MILLISECONDS);
        }

        SoundPacket packet = getNext();
        if (packet != null) {
            return packet;
        }
        packet = queue.poll(5, TimeUnit.MILLISECONDS);
        if (packet == null) {
            return null;
        }
        if (packet.getIndex() == lastSequenceNumber + 1 || lastSequenceNumber < 0) {
            lastSequenceNumber = packet.getIndex();
            return packet;
        } else {
            addSorted(packet);
            return null;
        }
    }

    private void addSorted(SoundPacket packet) {
        if (packet.getData().length <= 0) {
            isFlushingBuffer = true;
        }
        packetBuffer.add(packet);
        packetBuffer.sort(Comparator.comparingLong(SoundPacket::getIndex));
    }

    @Nullable
    private SoundPacket getNext() {
        if (isFlushingBuffer) {
            if (packetBuffer.isEmpty()) {
                isFlushingBuffer = false;
                return null;
            }
            return getFirstPacket();
        } else if (packetBuffer.size() > packetThreshold) {
            return getFirstPacket();
        } else if (!packetBuffer.isEmpty()) {
            SoundPacket packet = packetBuffer.get(0);
            if (packet.getIndex() == lastSequenceNumber + 1 || lastSequenceNumber < 0) {
                return getFirstPacket();
            }
            return null;
        }
        return null;
    }

    private SoundPacket getFirstPacket() {
        SoundPacket packet = packetBuffer.remove(0);
        lastSequenceNumber = packet.getIndex();
        return packet;
    }

    public void clear() {
        if (packetBuffer != null) {
            packetBuffer.clear();
        }
        lastSequenceNumber = -1L;
        isFlushingBuffer = false;
    }

    public int getSize() {
        if (packetBuffer == null) {
            return 0;
        }
        return packetBuffer.size();
    }

}