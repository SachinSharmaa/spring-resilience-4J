package com.example.resilience.poc.service;

import com.example.resilience.poc.config.Resilience4JConfig;
import com.example.resilience.poc.constants.CoreConstants;
import com.example.resilience.poc.constants.FallbackConstants;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Assumptions: Minimum number of calls per sliding window is less than sliding window size.
//              All test cases are for failure rate threshold 100.
@SpringBootTest
@ConfigurationProperties("resilience")
@Getter
@Setter
public class DemoServiceTest {

    @Autowired
    DemoService demoService;

    @Autowired
    Resilience4JConfig resilience4JConfig;

    private float failureRateThreshold = 60;

    private long slowCallDurationThreshold = 4000;

    private long waitDurationInOpenState = 20000;

    private int minimumNumberOfCalls = 2;

    private int slidingWindowSize = 2;

    private int permittedNumberOfCallsInHalfOpenState = 2;

    private long timeoutDuration = 10;

    @Test
    @DirtiesContext
    public void slowCallFallbackTestForReturnType() {
        int regularCount = 0, fallBackCount = 0;
        for (int i = 0; i < Math.min(slidingWindowSize,minimumNumberOfCalls) + 3; i++) {
            if (demoService.returningProcess(slowCallDurationThreshold + 1000).equals(CoreConstants.MESSAGE.getValue())) {
                regularCount++;
            } else {
                fallBackCount++;
            }
        }
        assertEquals(Math.min(slidingWindowSize,minimumNumberOfCalls), regularCount);
        assertEquals(3, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void slowCallFallbackTestForVoid() {
        int regularCount = 0, fallBackCount = 0;
        for (int i = 0; i < Math.min(slidingWindowSize,minimumNumberOfCalls) + 3; i++) {
            try {
                demoService.voidProcess(slowCallDurationThreshold + 1000);
                regularCount += 1;
            } catch (Exception e) {
                fallBackCount += 1;
            }
        }
        assertEquals(Math.min(slidingWindowSize,minimumNumberOfCalls), regularCount);
        assertEquals(3, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void uncheckedExceptionFallbackTest() {
        try {
            demoService.uncheckedExceptionProcess();
        } catch (Throwable throwable) {
            assertEquals(FallbackConstants.GENERIC_FALLBACK_MESSAGE.getValue(), throwable.getMessage());
        }
    }

    private void circuitSwitchHelper() {
        while (true) {
            try {
                demoService.voidProcess(slowCallDurationThreshold + 1000);
            } catch (Throwable throwable) {
                break;
            }
        }
        System.out.println("Circuit Open");
        assertEquals(FallbackConstants.CUSTOM_FALLBACK_MESSAGE.getValue(), demoService.returningProcess(0));
        System.out.println("Waiting for thread to transition to half-open state");
        try {
            Thread.sleep(waitDurationInOpenState);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DirtiesContext
    public void circuitSwitchToHalfOpenTest() {
        circuitSwitchHelper();
        assertEquals(CoreConstants.MESSAGE.getValue(), demoService.returningProcess(0));
    }

    @Test
    @DirtiesContext
    public void circuitSwitchFromHalfOpenToOpenTest() {
        int regularCount = 0, fallBackCount = 0;
        circuitSwitchHelper();
        for (int i = 0; i < permittedNumberOfCallsInHalfOpenState + 1; i++) {
            try {
                demoService.voidProcess(slowCallDurationThreshold + 1000);
                regularCount++;
            } catch (Throwable throwable) {
                fallBackCount++;
            }
        }
        assertEquals(permittedNumberOfCallsInHalfOpenState, regularCount);
        assertEquals(1, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void circuitSwitchFromHalfOpenToClosedTest() {
        int regularCount = 0, fallBackCount = 0;
        circuitSwitchHelper();
        for (int i = 0; i < permittedNumberOfCallsInHalfOpenState + 1; i++) {
            if (demoService.returningProcess(0).equals(CoreConstants.MESSAGE.getValue())) {
                regularCount++;
            } else {
                fallBackCount++;
            }
        }
        assertEquals(permittedNumberOfCallsInHalfOpenState + 1, regularCount);
        assertEquals(0, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void timeLimiterTest() {
        assertEquals(FallbackConstants.CUSTOM_FALLBACK_MESSAGE.getValue(), demoService.returningProcess(timeoutDuration * 1000 + 1000));
    }

}
