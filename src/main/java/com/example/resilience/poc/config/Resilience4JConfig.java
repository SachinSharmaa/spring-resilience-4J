package com.example.resilience.poc.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import java.time.Duration;
import java.util.Optional;


public abstract class Resilience4JConfig {

    protected float failureRateThreshold = 60;

    protected long slowCallDurationThreshold = 4000;

    protected long waitDurationInOpenState = 20000;

    protected int minimumNumberOfCalls = 2;

    protected int slidingWindowSize = 2;

    protected int permittedNumberOfCallsInHalfOpenState = 2;

    protected long timeoutDuration = 10;

    protected Customizer<Resilience4JCircuitBreakerFactory> createCircuitBreaker(Optional<String> circuitBreakerName) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold)
                .slowCallDurationThreshold(Duration.ofMillis(slowCallDurationThreshold))
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenState))
                .minimumNumberOfCalls(minimumNumberOfCalls)
                .slidingWindowSize(slidingWindowSize)
                .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                .build();
        if (circuitBreakerName.isPresent() && !circuitBreakerName.get().isBlank()) {
            return factory -> factory.configure(builder -> builder.timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build(), circuitBreakerName.get());
        }
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

    protected abstract Customizer<Resilience4JCircuitBreakerFactory> createConfigBean();

}
