package com.example.resilience.poc.config.fallback;

public class SuccessFallback extends GenericFallback{

    public static String fallBack(Throwable throwable) {
        return "FAILED";
    }
}
