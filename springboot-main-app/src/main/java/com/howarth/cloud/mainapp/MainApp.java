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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling
public class MainApp extends SpringBootServletInitializer  {

	/**
	 * Bean required for encrpyting passwords - this is used for our sign-up and login
	 * @return a password encoder
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/**
	 * This is required for running the app locally.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}


	/**
	 * This is required for when the app is hosted on a tomcat server.
	 * @param builder
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainApp.class);
	}

	/**
	 * This is used to create the directory that is used for uploading wars into.
	 * @param storageService
	 * @return
	 */
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
