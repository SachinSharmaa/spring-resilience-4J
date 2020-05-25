package com.example.resilience.poc.constants;

public enum FallbackConstants {

    GENERIC_FALLBACK_MESSAGE("Reverting to Generic Fallback"),
    CUSTOM_FALLBACK_MESSAGE("FAILED");

    private final String value;

    FallbackConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
