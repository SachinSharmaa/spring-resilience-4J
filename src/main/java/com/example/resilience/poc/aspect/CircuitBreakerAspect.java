package com.example.resilience.poc.aspect;

import com.example.resilience.poc.annotation.EnableCircuitBreaker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class CircuitBreakerAspect {

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @SuppressWarnings("unchecked")
    @Around(value = "@annotation(com.example.resilience.poc.annotation.EnableCircuitBreaker)")
    public Object addCircuitBreaker(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnableCircuitBreaker annotation = method.getAnnotation(EnableCircuitBreaker.class);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(annotation.name());
        Method fallbackMethod = annotation.fallbackClass().getMethod("fallBack", Throwable.class);
            Optional<Object> response = (Optional<Object>) circuitBreaker.run(() -> {
                        try {
                            return Optional.ofNullable(joinPoint.proceed());
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable.getCause());
                        }
                    }
                    ,throwable -> {
                        try {
                            return Optional.ofNullable(fallbackMethod.invoke(null,throwable));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            return Optional.ofNullable(new RuntimeException("Error while invoking fallback"));
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
