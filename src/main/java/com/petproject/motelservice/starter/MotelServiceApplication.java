package com.petproject.motelservice.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = "com.petproject.motelservice")
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MotelServiceApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MotelServiceApplication.class, args);
	}

}
