# resilience-poc

A POC to demonstrate use of Spring Cloud Circuit Breaker and Spring AOP to implement Resilience Circuit Breaker by creating a configurable annotation.

## [Resilience](https://resilience4j.readme.io/docs/getting-started)

Resilience4j is a lightweight, easy-to-use fault tolerance library inspired by
Netflix Hystrix, but designed for Java 8 and functional programming. Lightweight, because the library only uses Vavr, which does not have any other external library dependencies. Netflix Hystrix, in contrast, has a compile dependency to Archaius which has many more external library dependencies such as Guava and Apache Commons Configuration.

Resilience4j provides higher-order functions (decorators) to enhance any functional interface, lambda expression or method reference with a Circuit Breaker, Rate Limiter, Retry or Bulkhead. You can stack more than one decorator on any functional interface, lambda expression or method reference. The advantage is that you have the choice to select the decorators you need and nothing else.

## [Spring Cloud Circuit Breaker](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/1.0.0.RELEASE/reference/html/)

