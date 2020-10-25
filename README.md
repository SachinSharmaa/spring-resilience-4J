# resilience

A utility to enable use of Spring Cloud Circuit Breaker and AspectJ to implement Resilience Circuit Breaker by creating a configurable annotation.

## [Resilience](https://resilience4j.readme.io/docs/getting-started)

Resilience4j is a lightweight, easy-to-use fault tolerance library inspired by
Netflix Hystrix, but designed for Java 8 and functional programming. Lightweight, because the library only uses Vavr, which does not have any other external library dependencies. Netflix Hystrix, in contrast, has a compile dependency to Archaius which has many more external library dependencies such as Guava and Apache Commons Configuration.

Resilience4j provides higher-order functions (decorators) to enhance any functional interface, lambda expression or method reference with a Circuit Breaker, Rate Limiter, Retry or Bulkhead. You can stack more than one decorator on any functional interface, lambda expression or method reference. The advantage is that you have the choice to select the decorators you need and nothing else.

## [Spring Cloud Circuit Breaker](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/1.0.0.RELEASE/reference/html/)

Spring Cloud Hoxton.SR4 provides a starter for the Resilience4J implementation. This starter has been used to develop a configurable annotation that can be used to easily integrate Resilience 4J in any application.

## How To Use

To use this, we have to include the jar as a dependency in our project. Please make sure the artifact is installed in your local or remote repository.

1. Add the following dependency to your maven project.

    <groupId>com.sachin</groupId>
    <artifactId>resilience</artifactId>
    <version>0.0.1</version>

2. Import the default configurations via com.sachin.resilience.config.DefaultResilienceConfig.

3. Now annotate any method with com.sachin.resilience.annotation.EnableCircuitBreaker and done. The default fallbacks will be used for your methods.

The default configuration can be found in: com.sachin.resilience.properties.Resilience4JProperties and the default fallbacks are handeled by com.sachin.resilience.config.fallback.GenericFallback.

## How To Customise

The annotation @EnableCircuitBreaker accepts two inputs:

1. name: Name of the circuit breaker. The default is "circuitBreaker", which uses the default configuration for Resilience4J as described above. We can define a custom circuit breaker with different properties and specify the name of the circuit breaker here.

2. fallbackClass: An implementation class of com.sachin.resilience.config.fallback.Fallback defining the method to be called for fallback.

```java
public class CustomFallback implements Fallback {
    @Override
    public Object fallBack(Throwable throwable) {
        // handle fallback
        return throwable;
    }
}
```

### Defining custom CircuitBreaker

To define a custom circuit breaker

1 We need to extend com.sachin.resilience.properties.Resilience4JProperties and define the properties.

```java
@Configuration
@ConfigurationProperties("custom")
public class CustomResilience4JProperties extends Resilience4JProperties {
}
```

2 Create a bean of org.springframework.cloud.client.circuitbreaker.Customizer by extending com.sachin.resilience.config.Resilience4JConfig using an object of any child of com.sachin.resilience.properties.Resilience4JProperties. Also we need to override  #createConfigBean, annotate method with @Bean('name of circuit breaker') and call #createCircuitBreaker with name of the circuit breaker.

```java
@Configuration
public class CustomResilienceConfiguration extends Resilience4JConfig {

    protected CustomResilienceConfiguration(@Qualifier("customResilienceProperties") Resilience4JProperties properties) {
        super(properties);
    }

    @Override
    @Bean("custom")
    protected Customizer<Resilience4JCircuitBreakerFactory> createConfigBean() {
        return this.createCircuitBreaker(Optional.of("custom"));
    }
}
```

3 This is it now we can pass the name of the circuit breaker to @EnableCircuitBreaker and use our own circuitBreaker.

```java
@EnableCircuitBreaker(name = "custom", fallbackClass = CustomFallback.class)
```

### Suggestions/Feedbacks

I would love to hear any sort of feedback on this or would love to discuss anything related to resiliency, microservices or software engineering in general. Please feel free to try this out and reachout to me.

Write to me at sachinsharmaa15@gmail.com
Connect with me on [LinkedIn](https://www.linkedin.com/in/sachinsharmaa15/)