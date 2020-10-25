package com.sachin.resilience.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties("resilience")
@Getter
@Setter
@Primary
public class Resilience4JProperties {

    private float failureRateThreshold = 50;

    private long slowCallDurationThreshold = 4000;

    private long waitDurationInOpenState = 2000;

    private int minimumNumberOfCalls = 2;

    private int slidingWindowSize = 2;

    private int permittedNumberOfCallsInHalfOpenState = 2;

    private long timeoutDuration = 10;
}
