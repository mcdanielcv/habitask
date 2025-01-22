package com.habitask.config;

import com.habitask.utilitarian.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
