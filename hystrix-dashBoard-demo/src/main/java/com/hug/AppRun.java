package com.hug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hug"})
public class AppRun
{
    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }
}
