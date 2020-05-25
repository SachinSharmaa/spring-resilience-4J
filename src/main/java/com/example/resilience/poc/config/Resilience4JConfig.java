package com.example.resilience.poc.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfig {

    @Value("${resilience.failure.rate.threshold:50}")
    private float failureRateThreshold;

    @Value("${resilience.slow.call.duration.threshold:4000}")
    private long slowCallDurationThreshold;

    @Value("${resilience.wait.duration.in.open.state:20000}")
    private long waitDurationInOpenState;

    @Value("${resilience.minimum.number.of.calls:2}")
    private int minimumNumberOfCalls;

    @Value("${resilience.sliding.window.size:2}")
    private int slidingWindowSize;

    @Value("${resilience.permitted.number.of.calls.in.half.open.state:2}")
    private int permittedNumberOfCallsInHalfOpenState;

    @Value("${resilience.time.out.duration:10}")
    private long timeoutDuration;

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
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

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
}
