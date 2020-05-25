package com.example.resilience.poc;

import com.example.resilience.poc.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResiliencePocApplicationTests {

	@Autowired
	DemoService demoService;

	@Test
	void contextLoads() {
	}

}
