package com.example.resilience.poc.service;

import com.example.resilience.poc.annotation.EnableCircuitBreaker;
import com.example.resilience.poc.config.fallback.CustomFallback;
import com.example.resilience.poc.config.fallback.SuccessFallback;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @EnableCircuitBreaker(fallbackClass = CustomFallback.class)
    public void voidProcess(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @EnableCircuitBreaker
    public void uncheckedExceptionProcess() {
        int a = 0;
        int b = 10 / a;
    }

    @Override
    @EnableCircuitBreaker(fallbackClass = SuccessFallback.class)
    public String returningProcess(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

}
