package com.max420.maxlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"com.max420.MaxLibrary","config", "models", "dto", "controllers", "services", "repository"})
@EnableJpaRepositories(basePackages = "repository")
@ComponentScan(basePackages = {"config", "controllers", "repository", "services"})
@EntityScan(basePackages = "models")
@EnableWebSecurity
public class MaxLibraryApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaxLibraryApplication.class, args);
	}
}