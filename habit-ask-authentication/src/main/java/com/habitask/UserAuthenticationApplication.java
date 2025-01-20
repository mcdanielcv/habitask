package com.habitask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class UserAuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthenticationApplication.class, args);
    }
}
