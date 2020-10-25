package com.sachin.resilience.config;

import com.sachin.resilience.properties.Resilience4JProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import java.time.Duration;
import java.util.Optional;


public abstract class Resilience4JConfig {

   private final Resilience4JProperties properties;

   protected Resilience4JConfig(Resilience4JProperties properties) {
       this.properties = properties;
   }

    protected Customizer<Resilience4JCircuitBreakerFactory> createCircuitBreaker(Optional<String> circuitBreakerName) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(properties.getFailureRateThreshold())
                .slowCallDurationThreshold(Duration.ofMillis(properties.getSlowCallDurationThreshold()))
                .waitDurationInOpenState(Duration.ofMillis(properties.getWaitDurationInOpenState()))
                .minimumNumberOfCalls(properties.getMinimumNumberOfCalls())
                .slidingWindowSize(properties.getSlidingWindowSize())
                .permittedNumberOfCallsInHalfOpenState(properties.getPermittedNumberOfCallsInHalfOpenState())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(properties.getTimeoutDuration()))
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
