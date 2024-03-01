package spcore.fabric.sounds.managers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import spcore.GlobalContext;
import spcore.fabric.sounds.utils.NamedThreadPoolFactory;
import spcore.fabric.sounds.utils.Utils;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioInputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AudioManager {
    protected final MinecraftClient mc;
    protected final SpCoreSoundManager soundManager;
    protected final int sampleRate;
    protected int bufferSize;
    protected int bufferSampleSize;
    protected int source;
    protected volatile int bufferIndex;
    protected final int[] buffers;
    protected final ExecutorService executor;

    protected long audioChannelId;

    public AudioManager(SpCoreSoundManager soundManager, int sampleRate, int bufferSize, long audioChannelId) {
        mc = MinecraftClient.getInstance();
        this.soundManager = soundManager;
        this.sampleRate = sampleRate;
        this.bufferSize = bufferSize;
        this.bufferSampleSize = bufferSize;
        this.audioChannelId = audioChannelId;
        this.buffers = new int[32];
        String threadName = "SPNET-SoundSourceThread-%s".formatted(audioChannelId);
        executor = Executors.newSingleThreadExecutor(NamedThreadPoolFactory.create(threadName));
    }

    public void open() throws Exception {
        runInContext(this::openSync);
    }

    protected void openSync() {
        if (hasValidSourceSync()) {
            return;
        }
        source = AL11.alGenSources();
        SpCoreSoundManager.checkAlError();
        AL11.alSourcei(source, AL11.AL_LOOPING, AL11.AL_FALSE);
        SpCoreSoundManager.checkAlError();

        AL11.alDistanceModel(AL11.AL_LINEAR_DISTANCE);
        SpCoreSoundManager.checkAlError();
        AL11.alSourcef(source, AL11.AL_MAX_DISTANCE, Utils.getDefaultDistance());
        SpCoreSoundManager.checkAlError();
        AL11.alSourcef(source, AL11.AL_REFERENCE_DISTANCE, 0F);
        SpCoreSoundManager.checkAlError();

        AL11.alGenBuffers(buffers);
        SpCoreSoundManager.checkAlError();

    }

    public void play(short[] data, float volume, Vec3d position, @Nullable String category, float maxDistance) {
        runInContext(() -> {
            removeProcessedBuffersSync();
            boolean stopped = isStoppedSync();
            if (stopped) {
                GlobalContext.LOGGER.debug("Filling playback buffer {}", audioChannelId);
                for (int i = 0; i < getBufferSize(); i++) {
                    writeSync(new short[bufferSize], 1F, position, category, maxDistance);
                }
            }

            writeSync(data, volume, position, category, maxDistance);

            if (stopped) {
                AL11.alSourcePlay(source);
                SpCoreSoundManager.checkAlError();
            }
        });
    }

    protected boolean isStoppedSync() {
        return getStateSync() == AL11.AL_INITIAL || getStateSync() == AL11.AL_STOPPED || getQueuedBuffersSync() <= 0;
    }

    protected int getBufferSize() {
        return 5;
    }

    protected int getStateSync() {
        int state = AL11.alGetSourcei(source, AL11.AL_SOURCE_STATE);
        SpCoreSoundManager.checkAlError();
        return state;
    }

    protected void writeSync(short[] data, float volume, @Nullable Vec3d position, @Nullable String category, float maxDistance) {
        setPositionSync(position, maxDistance);

        AL11.alSourcef(source, AL11.AL_MAX_GAIN, 6F);
        SpCoreSoundManager.checkAlError();
        AL11.alSourcef(source, AL11.AL_GAIN, getVolume(volume, position, maxDistance));
        SpCoreSoundManager.checkAlError();
        AL11.alListenerf(AL11.AL_GAIN, 1F);
        SpCoreSoundManager.checkAlError();

        int queuedBuffers = getQueuedBuffersSync();
        if (queuedBuffers >= buffers.length) {
            GlobalContext.LOGGER.warn("Full playback buffer: {}/{}", queuedBuffers, buffers.length);
            int sampleOffset = AL11.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET);
            SpCoreSoundManager.checkAlError();
            int buffersToSkip = queuedBuffers - getBufferSize();
            AL11.alSourcei(source, AL11.AL_SAMPLE_OFFSET, sampleOffset + buffersToSkip * bufferSampleSize);
            SpCoreSoundManager.checkAlError();
            removeProcessedBuffersSync();
        }

        AL11.alBufferData(buffers[bufferIndex], getFormat(), convert(data, position), sampleRate);
        SpCoreSoundManager.checkAlError();
        AL11.alSourceQueueBuffers(source, buffers[bufferIndex]);
        SpCoreSoundManager.checkAlError();
        bufferIndex = (bufferIndex + 1) % buffers.length;
    }

    public void close() {
        runInContext(this::closeSync);
    }

    protected void closeSync() {
        if (hasValidSourceSync()) {
            if (getStateSync() == AL11.AL_PLAYING) {
                AL11.alSourceStop(source);
                SpCoreSoundManager.checkAlError();
            }

            AL11.alDeleteSources(source);
            SpCoreSoundManager.checkAlError();
            AL11.alDeleteBuffers(buffers);
            SpCoreSoundManager.checkAlError();
        }
        source = 0;
        executor.shutdown();
    }

    public void checkBufferEmpty(Runnable onEmpty) {
        runInContext(() -> {
            if (getStateSync() == AL11.AL_STOPPED || getQueuedBuffersSync() <= 0) {
                onEmpty.run();
            }
        });
    }

    protected void removeProcessedBuffersSync() {
        int processed = AL11.alGetSourcei(source, AL11.AL_BUFFERS_PROCESSED);
        SpCoreSoundManager.checkAlError();
        for (int i = 0; i < processed; i++) {
            AL11.alSourceUnqueueBuffers(source);
            SpCoreSoundManager.checkAlError();
        }
    }

    protected int getQueuedBuffersSync() {
        int buffers = AL11.alGetSourcei(source, AL11.AL_BUFFERS_QUEUED);
        SpCoreSoundManager.checkAlError();
        return buffers;
    }

    protected boolean hasValidSourceSync() {
        boolean validSource = AL11.alIsSource(source);
        SpCoreSoundManager.checkAlError();
        return validSource;
    }

    public void fetchQueuedBuffersAsync(Consumer<Integer> supplier) {
        runInContext(() -> {
            if (isStoppedSync()) {
                supplier.accept(-1);
                return;
            }
            supplier.accept(getQueuedBuffersSync());
        });
    }

    protected float getVolume(float volume, @Nullable Vec3d position, float maxDistance) {
        return volume;
    }

    protected void linearAttenuation(float maxDistance) {
        AL11.alDistanceModel(AL11.AL_LINEAR_DISTANCE);
        SpCoreSoundManager.checkAlError();

        AL11.alSourcef(source, AL11.AL_MAX_DISTANCE, maxDistance);
        SpCoreSoundManager.checkAlError();

        AL11.alSourcef(source, AL11.AL_REFERENCE_DISTANCE, maxDistance / 2F);
        SpCoreSoundManager.checkAlError();
    }
    protected int getFormat(){
        return AL11.AL_FORMAT_STEREO16;
    }

    protected short[] convert(short[] data, @Nullable Vec3d position) {
        return data;
    }


    protected void setPositionSync(@Nullable Vec3d soundPos, float maxDistance) {
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d position = camera.getPos();
        Vector3f look = camera.getDiagonalPlane();
        Vector3f up = camera.getHorizontalPlane();
        AL11.alListener3f(AL11.AL_POSITION, (float) position.x, (float) position.y, (float) position.z);
        SpCoreSoundManager.checkAlError();
        AL11.alListenerfv(AL11.AL_ORIENTATION, new float[]{look.x(), look.y(), look.z(), up.x(), up.y(), up.z()});
        SpCoreSoundManager.checkAlError();
        if (soundPos != null) {
            linearAttenuation(maxDistance);
            AL11.alSourcei(source, AL11.AL_SOURCE_RELATIVE, AL11.AL_FALSE);
            SpCoreSoundManager.checkAlError();
            AL11.alSource3f(source, AL11.AL_POSITION, (float) soundPos.x, (float) soundPos.y, (float) soundPos.z);
            SpCoreSoundManager.checkAlError();
        } else {
            linearAttenuation(48F);
            AL11.alSourcei(source, AL11.AL_SOURCE_RELATIVE, AL11.AL_TRUE);
            SpCoreSoundManager.checkAlError();
            AL11.alSource3f(source, AL11.AL_POSITION, 0F, 0F, 0F);
            SpCoreSoundManager.checkAlError();
        }
    }


    public void runInContext(Runnable runnable) {
        if (executor.isShutdown()) {
            return;
        }
        soundManager.runInContext(executor, runnable);
    }
}
