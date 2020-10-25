package com.sachin.resilience.aspect;

import com.sachin.resilience.annotation.EnableCircuitBreaker;
import com.sachin.resilience.config.fallback.Fallback;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class CircuitBreakerAspect {

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @SuppressWarnings("unchecked")
    @Around(value = "@annotation(com.sachin.resilience.annotation.EnableCircuitBreaker)")
    public Object addCircuitBreaker(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnableCircuitBreaker annotation = method.getAnnotation(EnableCircuitBreaker.class);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(annotation.name());
        Fallback fallbackClass = annotation.fallbackClass().getConstructor().newInstance();
            Optional<Object> response = circuitBreaker.run(() -> {
                        try {
                            return Optional.ofNullable(joinPoint.proceed());
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }
                    ,throwable -> {
                        try {
                            return Optional.ofNullable(fallbackClass.fallBack(throwable));
                        } catch (Exception e) {
                            return Optional.ofNullable(new RuntimeException("Error while invoking fallback",e));
                        }
                    });
         if(response.isPresent()) {
             if(Throwable.class.isAssignableFrom(response.get().getClass())) {
                 throw (Throwable) response.get();
             } else {
                 return response.get();
             }
         } else {
             return null;
         }
    }
}
