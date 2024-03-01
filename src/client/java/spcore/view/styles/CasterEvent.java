package spcore.view.styles;

public interface CasterEvent {
    <T> T cast(String value, Class<T> clazz);
}
