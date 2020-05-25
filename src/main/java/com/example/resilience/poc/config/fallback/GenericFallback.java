package com.example.resilience.poc.config.fallback;

public class GenericFallback {

    public static Object fallBack(Throwable throwable) {
        return new RuntimeException("Reverting to Generic Fallback");
    }
}
