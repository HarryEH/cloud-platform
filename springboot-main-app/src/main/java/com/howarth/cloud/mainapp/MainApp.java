package com.howarth.cloud.mainapp;

import com.howarth.cloud.mainapp.uploads.storage.StorageProperties;
import com.howarth.cloud.mainapp.uploads.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MainApp extends SpringBootServletInitializer  {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainApp.class);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
