package com.example.resilience.poc.config.fallback;

public class CustomFallback extends GenericFallback {

    public static Object fallBack(Throwable throwable) {
        return new RuntimeException("Reverting to Custom Fallback");
    }
}
