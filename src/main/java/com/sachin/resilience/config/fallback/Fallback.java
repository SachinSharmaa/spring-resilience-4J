package com.sachin.resilience.config.fallback;

public interface Fallback {
    Object fallBack(Throwable throwable);
}
