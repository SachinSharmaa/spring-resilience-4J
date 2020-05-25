package com.example.resilience.poc.constants;

public enum CoreConstants {

    MESSAGE("SUCCESS");

    private final String value;

    CoreConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
