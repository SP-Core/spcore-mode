package spcore.fabric.sounds.managers;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTThreadLocalContext;
import spcore.GlobalContext;

import javax.annotation.Nullable;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpCoreSoundManager {
    public static final int SAMPLE_RATE = 48000;
    public static final int FRAME_SIZE = (SAMPLE_RATE / 1000) * 20;
    public static final int MAX_PAYLOAD_SIZE = 1024;

    @Nullable
    private final String deviceName;
    private long device;
    private long context;

    public SpCoreSoundManager(@Nullable String deviceName) throws Exception {
        this.deviceName = deviceName;

        device = openSpeaker(deviceName);
        context = ALC11.alcCreateContext(device, (IntBuffer) null);
    }

    public void close() {
        if (context != 0L) {
            ALC11.alcDestroyContext(context);
            checkAlcError(device);
        }
        if (device != 0L) {
            ALC11.alcCloseDevice(device);
            checkAlcError(device);
        }
        context = 0;
        device = 0;
    }

    public boolean isClosed() {
        return context == 0 || device == 0;
    }

    private long openSpeaker(@Nullable String name) throws Exception {
        try {
            return tryOpenSpeaker(name);
        } catch (Exception e) {
            if (name != null) {
                GlobalContext.LOGGER.warn("Failed to open audio channel '{}', falling back to default", name);
            }
            try {
                return tryOpenSpeaker(getDefaultSpeaker());
            } catch (Exception ex) {
                return tryOpenSpeaker(null);
            }
        }
    }

    private long tryOpenSpeaker(@Nullable String string) throws Exception {
        long l = ALC11.alcOpenDevice(string);
        if (l == 0L) {
            throw new Exception("Failed to open audio device: Audio device not found");
        }
        checkAlcError(device);
        return l;
    }

    @Nullable
    public static String getDefaultSpeaker() {
        if (!canEnumerate()) {
            return null;
        }
        String defaultSpeaker = ALC11.alcGetString(0L, ALC11.ALC_ALL_DEVICES_SPECIFIER);
        checkAlcError(0L);
        return defaultSpeaker;
    }

    public static List<String> getAllSpeakers() {
        if (!canEnumerate()) {
            return Collections.emptyList();
        }
        List<String> devices = ALUtil.getStringList(0L, ALC11.ALC_ALL_DEVICES_SPECIFIER);
        checkAlcError(0L);
        return devices == null ? Collections.emptyList() : devices;
    }

    public void runInContext(Executor executor, Runnable runnable) {
        long time = System.currentTimeMillis();
        executor.execute(() -> {
            long diff = System.currentTimeMillis() - time;
            if (diff > 20 || (diff >= 5 && true)) {
                GlobalContext.LOGGER.warn("Sound executor delay: {} ms!", diff);
            }
            if (openContext()) {
                runnable.run();
                closeContext();
            }
        });
    }

    public boolean openContext() {
        if (context == 0) {
            return false;
        }
        boolean success = EXTThreadLocalContext.alcSetThreadContext(context);
        checkAlcError(device);
        return success;
    }

    public void closeContext() {
        EXTThreadLocalContext.alcSetThreadContext(0L);
        checkAlcError(device);
    }

    public static boolean checkAlError() {
        int error = AL11.alGetError();
        if (error == AL11.AL_NO_ERROR) {
            return false;
        }
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        GlobalContext.LOGGER.error("Voicechat sound manager AL error: {}.{}[{}] {}", stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), getAlError(error));
        return true;
    }

    public static boolean checkAlcError(long device) {
        int error = ALC11.alcGetError(device);
        if (error == ALC11.ALC_NO_ERROR) {
            return false;
        }
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        GlobalContext.LOGGER.error("Voicechat sound manager ALC error: {}.{}[{}] {}", stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), getAlcError(error));
        return true;
    }

    private static String getAlError(int i) {
        switch (i) {
            case AL11.AL_INVALID_NAME:
                return "Invalid name";
            case AL11.AL_INVALID_ENUM:
                return "Invalid enum ";
            case AL11.AL_INVALID_VALUE:
                return "Invalid value";
            case AL11.AL_INVALID_OPERATION:
                return "Invalid operation";
            case AL11.AL_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error";
        }
    }

    public static String getAlcError(int i) {
        switch (i) {
            case ALC11.ALC_INVALID_DEVICE:
                return "Invalid device";
            case ALC11.ALC_INVALID_CONTEXT:
                return "Invalid context";
            case ALC11.ALC_INVALID_ENUM:
                return "Invalid enum";
            case ALC11.ALC_INVALID_VALUE:
                return "Invalid value";
            case ALC11.ALC_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error";
        }
    }

    private static final Pattern DEVICE_NAME = Pattern.compile("^(?:OpenAL.+?on )?(.*)$");

    public static String cleanDeviceName(String name) {
        Matcher matcher = DEVICE_NAME.matcher(name);
        if (!matcher.matches()) {
            return name;
        }
        return matcher.group(1);
    }

    public static boolean canEnumerate() {
        boolean present = ALC11.alcIsExtensionPresent(0L, "ALC_ENUMERATE_ALL_EXT");
        checkAlcError(0L);
        return present;
    }

}
