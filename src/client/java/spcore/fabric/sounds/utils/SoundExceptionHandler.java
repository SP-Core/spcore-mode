package spcore.fabric.sounds.utils;

import spcore.GlobalContext;

public class SoundExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        GlobalContext.LOGGER.error("Uncaught exception in thread {}", t.getName(), e);
    }
}
