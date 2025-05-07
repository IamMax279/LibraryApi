package com.max420.books;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {
		"com.max420.books",
		"com.max420.books.config",
		"com.max420.books.controllers",
		"com.max420.books.models",
		"com.max420.books.repository",
		"com.max420.books.services",
})
@EntityScan(basePackages = {"com.max420.books.models"})
@ComponentScan(basePackages = {
		"com.max420.books.controllers",
		"com.max420.books.repository",
		"com.max420.books.services",
		"com.max420.books.config",
})
@EnableJpaRepositories(basePackages = "com.max420.books.repository")
public class BooksApplication {
	private static final String port = "8081";
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BooksApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", port));
		app.run(args);
	}
}
