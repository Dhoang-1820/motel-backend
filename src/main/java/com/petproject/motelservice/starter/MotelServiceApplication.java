package com.petproject.motelservice.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.petproject.motelservice"}, exclude = { SecurityAutoConfiguration.class})
public class MotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotelServiceApplication.class, args);
	}

}
