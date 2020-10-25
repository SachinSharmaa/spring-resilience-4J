package com.sachin.resilience.config;

import com.sachin.resilience.properties.Resilience4JProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Configuration
@Primary
@ComponentScan(basePackages = "com.sachin.resilience")
@Getter
@Setter
public class DefaultResilienceConfig extends Resilience4JConfig {

    public DefaultResilienceConfig(Resilience4JProperties properties) {
        super(properties);
    }

    @Override
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> createConfigBean() {
        return createCircuitBreaker(Optional.empty());
    }

}
