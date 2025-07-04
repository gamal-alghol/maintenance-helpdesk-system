package com.example.MaintenanceHelpdesk.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.MaintenanceHelpdesk.user_service.entity")
@EnableJpaRepositories(basePackages = "com.example.MaintenanceHelpdesk.user_service.repository")
@ComponentScan(basePackages = "com.example.MaintenanceHelpdesk")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}


