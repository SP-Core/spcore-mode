package spcore;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class SpCoreLogger implements Logger {
    private final Logger logger = LoggerFactory.getLogger(GlobalContext.MOD_ID);

    public boolean LoggingChat = false;
    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
        logChat("trace", msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, arg);
        logChat("trace", format);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, arg1, arg2);
        logChat("trace", format);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
        logChat("trace", format);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
        logChat("trace", msg);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        logger.trace(marker, s);
        logChat("trace", s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        logger.trace(marker, s, o);
        logChat("trace", s);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        logger.trace(marker, s, o, o1);
        logChat("trace", s);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        logger.trace(marker, s, objects);
        logChat("trace", s);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        logger.trace(marker, s, throwable);
        logChat("trace", s);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
        logChat("debug", msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, arg);
        logChat("debug", format);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
        logChat("debug", format);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
        logChat("debug", format);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
        logChat("debug", msg);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        logger.debug(marker, s);
        logChat("debug", s);

    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        logger.debug(marker, s, o);
        logChat("debug", s);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        logger.debug(marker, s, o, o1);
        logChat("debug", s);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        logger.debug(marker, s, objects);
        logChat("debug", s);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        logger.debug(marker, s, throwable);
        logChat("debug", s);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
        logChat("info", msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, arg);
        logChat("info", format);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
        logChat("info", format);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
        logChat("info", format);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
        logChat("info", msg);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        logger.info(marker, s);
        logChat("info", s);

    }

    @Override
    public void info(Marker marker, String s, Object o) {
        logger.info(marker, s, o);
        logChat("info", s);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        logger.info(marker, s, o, o1);
        logChat("info", s);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        logger.info(marker, s, objects);
        logChat("info", s);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        logger.info(marker, s, throwable);
        logChat("info", s);

    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
        logChat("warn", msg);

    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format, arg);
        logChat("warn", format);

    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
        logChat("warn", format);

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
        logChat("warn", format);

    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
        logChat("warn", msg);

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        logger.warn(marker, s);
        logChat("warn", s);

    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        logger.warn(marker, s, o);
        logChat("warn", s);

    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        logger.warn(marker, s, o, o1);
        logChat("warn", s);

    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        logger.warn(marker, s, objects);
        logChat("warn", s);

    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        logger.warn(marker, s, throwable);
        logChat("warn", s);

    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
        logChat("error", msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, arg);
        logChat("error", format);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
        logChat("error", format);

    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
        logChat("error", format);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
        logChat("error", msg);

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        logger.error(marker, s);
        logChat("error", s);

    }

    @Override
    public void error(Marker marker, String s, Object o) {
        logger.error(marker, s, o);
        logChat("error", s);

    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        logger.error(marker, s, o, o1);
        logChat("error", s);

    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        logger.error(marker, s, objects);
        logChat("error", s);

    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        logger.error(marker, s, throwable);
        logChat("error", s);
    }


    private void logChat(String level, String message){
        if(!LoggingChat){
            return;
        }
//        MinecraftClient.getInstance()
//                .inGameHud
//                .getChatHud().clear(false);
        MinecraftClient.getInstance()
                .inGameHud
                .getChatHud()
                .addMessage(Text.of("["+level+"] " + message));
    }
}
