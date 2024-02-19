package spcore.view.styles;

public interface CasterEvent {
    public <T> T cast(String value, Class<T> clazz);
}
