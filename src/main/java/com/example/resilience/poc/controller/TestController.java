package com.example.resilience.poc.controller;

import com.example.resilience.poc.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private DemoService demoService;

    public TestController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/test")
    public int test() {
        return demoService.add(1,2);

    }

    @GetMapping("/test/void")
    public int testVoid() {
        demoService.voidMethod();
        return 1;
    }

}