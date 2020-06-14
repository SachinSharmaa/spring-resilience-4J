package com.example.resilience.poc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Configuration
@Primary
@ConfigurationProperties("resilience")
@Getter
@Setter
public class DefaultResilienceConfig extends Resilience4JConfig {

    @Override
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> createConfigBean() {
        return createCircuitBreaker(Optional.empty());
    }

}
