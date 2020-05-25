package com.example.resilience.poc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DemoServiceTest {

    @Autowired
    DemoService demoService;

    @Test
    @DirtiesContext
    public void slowCallFallbackTestForReturnType() {
        int regularCount = 0, fallBackCount = 0;
        for (int i = 0; i < 5; i++) {
            try {
                demoService.add(1, 2);
                regularCount += 1;
            } catch (Exception e) {
                fallBackCount += 1;
            }
        }
        assertEquals(2, regularCount);
        assertEquals(3, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void slowCallFallbackTestForVoid() {
        int regularCount = 0, fallBackCount = 0;
        for (int i = 0; i < 5; i++) {
            try {
                demoService.voidMethod();
                regularCount += 1;
            } catch (Exception e) {
                fallBackCount += 1;
            }
        }
        assertEquals(2, regularCount);
        assertEquals(3, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void uncheckedExceptionFallbackTest() {
        try {
            demoService.uncheckedExceptionGenerator();
        } catch (Throwable throwable) {
            assertEquals("Reverting to Generic Fallback", throwable.getMessage());
        }
    }

    private void circuitSwitchHelper() {
        while (true) {
            try {
                demoService.voidMethod();
            } catch (Throwable throwable) {
                break;
            }
        }
        System.out.println("Circuit Open");
        assertEquals("FAILED", demoService.success());
        System.out.println("Waiting for thread to transition to half-open state");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DirtiesContext
    public void circuitSwitchToHalfOpenTest() {
        circuitSwitchHelper();
        assertEquals("SUCCESS", demoService.success());
    }

    @Test
    @DirtiesContext
    public void circuitSwitchFromHalfOpenToOpenTest() {
        int regularCount = 0, fallBackCount = 0;
        circuitSwitchHelper();
        for (int i = 0; i < 3; i++) {
            try {
                demoService.voidMethod();
                regularCount++;
            } catch (Throwable throwable) {
                fallBackCount++;
            }
        }
        assertEquals(2, regularCount);
        assertEquals(1, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void circuitSwitchFromHalfOpenToClosedTest() {
        int regularCount = 0, fallBackCount = 0;
        circuitSwitchHelper();
        for (int i = 0; i < 3; i++) {
            if (demoService.success().equals("SUCCESS")) {
                regularCount++;
            } else {
                fallBackCount++;
            }
        }
        assertEquals(3, regularCount);
        assertEquals(0, fallBackCount);
    }

    @Test
    @DirtiesContext
    public void timeLimiterTest() {
        assertEquals("FAILED", demoService.timeConsumingProcess());
    }

}
