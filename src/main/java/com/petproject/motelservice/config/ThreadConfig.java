package com.petproject.motelservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

	@Bean
	ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
		t.setCorePoolSize(10);
		t.setMaxPoolSize(100);
		t.setQueueCapacity(50);
		t.setAllowCoreThreadTimeOut(true);
		t.setKeepAliveSeconds(120);
		t.initialize();
		return t;
	}
}
