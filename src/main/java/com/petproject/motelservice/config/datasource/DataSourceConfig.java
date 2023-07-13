package com.petproject.motelservice.config.datasource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.petproject.motelservice.repository")
@EntityScan("com.petproject.motelservice.domain.inventory")
public class DataSourceConfig {

}
