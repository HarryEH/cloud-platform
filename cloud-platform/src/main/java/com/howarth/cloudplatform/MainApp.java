package com.howarth.cloudplatform;

import com.howarth.cloudplatform.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class MainApp extends SpringBootServletInitializer {

    /**
     * This is required for running the app locally.
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    /**
     * Bean required for encrpyting passwords - this is used for our sign-up and login
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This is required for when the app is hosted on a tomcat server.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApp.class);
    }

    /**
     * This is used to create the directory that is used for uploading wars into.
     */
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

}
