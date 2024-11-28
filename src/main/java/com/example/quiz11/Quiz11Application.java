package com.example.quiz11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Quiz11Application {

	public static void main(String[] args) {
		SpringApplication.run(Quiz11Application.class, args);
	}

}
