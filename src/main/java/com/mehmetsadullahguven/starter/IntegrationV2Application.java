package com.mehmetsadullahguven.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.mehmetsadullahguven"})
@EntityScan(basePackages = {"com.mehmetsadullahguven"})
@EnableJpaRepositories(basePackages = {"com.mehmetsadullahguven"})
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class IntegrationV2Application {
	public static void main(String[] args) {
		SpringApplication.run(IntegrationV2Application.class, args);
	}
}
