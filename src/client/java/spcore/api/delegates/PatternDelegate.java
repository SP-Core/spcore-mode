package spcore.api.delegates;

import spcore.api.models.DocumentPattern;

public interface PatternDelegate {
    public void Invoke(boolean success, DocumentPattern pattern);
}
