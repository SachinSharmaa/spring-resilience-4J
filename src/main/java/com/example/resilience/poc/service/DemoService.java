package com.example.resilience.poc.service;

public interface DemoService {

    public void voidProcess(long sleepTime);

    public void uncheckedExceptionProcess();

    public String returningProcess(long sleepTime);

}
