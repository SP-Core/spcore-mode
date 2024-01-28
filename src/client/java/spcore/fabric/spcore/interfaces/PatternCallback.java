package spcore.fabric.spcore.interfaces;

import spcore.fabric.spcore.models.DocumentPattern;

public interface PatternCallback {
    public void Invoke(boolean success, DocumentPattern pattern);
}
