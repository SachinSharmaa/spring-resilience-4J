package com.example.resilience.poc.config.fallback;

import com.example.resilience.poc.constants.FallbackConstants;

public class CustomFallback extends GenericFallback{

    public static String fallBack(Throwable throwable) {
        return FallbackConstants.CUSTOM_FALLBACK_MESSAGE.getValue();
    }
}
