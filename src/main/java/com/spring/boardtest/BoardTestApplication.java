package com.spring.boardtest;

import com.spring.boardtest.config.GoogleProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GoogleProperties.class)
public class BoardTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardTestApplication.class, args);
	}
}
