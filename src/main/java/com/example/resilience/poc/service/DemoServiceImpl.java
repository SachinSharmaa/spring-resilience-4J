package com.example.resilience.poc.service;

import com.example.resilience.poc.annotation.EnableCircuitBreaker;
import com.example.resilience.poc.config.fallback.CustomFallback;
import com.example.resilience.poc.config.fallback.SuccessFallback;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @EnableCircuitBreaker
    public int add(int a, int b) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;
    }

    @Override
    @EnableCircuitBreaker(fallbackClass = CustomFallback.class)
    public void voidMethod() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @EnableCircuitBreaker
    public void uncheckedExceptionGenerator() {
        int a = 0;
        int b = 10/a;
    }

    @Override
    @EnableCircuitBreaker(fallbackClass = SuccessFallback.class)
    public String success() {
        return "SUCCESS";
    }

    @Override
    @EnableCircuitBreaker(fallbackClass = SuccessFallback.class)
    public String timeConsumingProcess() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }


}
