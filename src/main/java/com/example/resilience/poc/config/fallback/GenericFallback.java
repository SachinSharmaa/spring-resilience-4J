package com.example.resilience.poc.config.fallback;

import com.example.resilience.poc.constants.FallbackConstants;

public class GenericFallback {

    public static Object fallBack(Throwable throwable) {
        return new RuntimeException(FallbackConstants.GENERIC_FALLBACK_MESSAGE.getValue());
    }
}
