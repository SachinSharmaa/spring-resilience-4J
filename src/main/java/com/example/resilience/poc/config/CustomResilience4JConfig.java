package com.example.resilience.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration("customResilience4JConfig")
@ConfigurationProperties("custom")
public class CustomResilience4JConfig extends Resilience4JConfig{

    @Bean("customConfiguration")
    @Override
    public Customizer<Resilience4JCircuitBreakerFactory> createConfigBean() {
        return createCircuitBreaker(Optional.of("customCircuitBreaker"));
    }
}
