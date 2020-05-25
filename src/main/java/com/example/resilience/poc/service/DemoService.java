package com.example.resilience.poc.service;

public interface DemoService {

    public int add(int a, int b);

    public void voidMethod();

    public void uncheckedExceptionGenerator();

    public String success();

    public String timeConsumingProcess();
}
