package com.example.spcore.interfaces;

import com.example.spcore.models.DocumentPattern;

public interface PatternCallback {
    public void Invoke(boolean success, DocumentPattern pattern);
}
