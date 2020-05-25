package com.example.resilience.poc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Assumptions: Minimum number of calls per sliding window is less than sliding window size.
//              All test cases are for failure rate threshold 100.
@SpringBootTest
public class DemoServiceTest {

    @Autowired
    DemoService demoService;

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

    @Test
    @DirtiesContext
    public void slowCallFallbackTestForReturnType() {
        int regularCount = 0, fallBackCount = 0;
        for (int i = 0; i < Math.min(slidingWindowSize,minimumNumberOfCalls) + 3; i++) {
            if (demoService.returningProcess(slowCallDurationThreshold + 1000).equals("SUCCESS")) {
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
            assertEquals("Reverting to Generic Fallback", throwable.getMessage());
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
        assertEquals("FAILED", demoService.returningProcess(0));
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
        assertEquals("SUCCESS", demoService.returningProcess(0));
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
            if (demoService.returningProcess(0).equals("SUCCESS")) {
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
        assertEquals("FAILED", demoService.returningProcess(timeoutDuration * 1000 + 1000));
    }

}
