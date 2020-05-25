package com.example.resilience.poc.annotation;

import com.example.resilience.poc.config.fallback.GenericFallback;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCircuitBreaker {
    String name() default "test";

    Class<? extends GenericFallback> fallbackClass() default GenericFallback.class;
}
