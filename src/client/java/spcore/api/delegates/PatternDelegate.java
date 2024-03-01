package spcore.api.delegates;

import spcore.api.models.DocumentPattern;

public interface PatternDelegate {
    void Invoke(boolean success, DocumentPattern pattern);
}
