package com.example.resilience.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ResiliencePocApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResiliencePocApplication.class, args);
	}

}
