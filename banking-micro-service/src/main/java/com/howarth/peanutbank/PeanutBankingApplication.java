package com.howarth.peanutbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class PeanutBankingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PeanutBankingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PeanutBankingApplication.class);
    }
}
