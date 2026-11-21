package com.devsecops.meteotn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeteoApplication {
    private static final String GITHUB_TOKEN = "ghp_1234567890abcdef1234567890abcdef1234";

    public static void main(String[] args) {
        SpringApplication.run(MeteoApplication.class, args);
    }
}