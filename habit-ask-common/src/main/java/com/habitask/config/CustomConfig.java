package com.habitask.config;

import com.habitask.utilitarian.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = "com.habitask.model") // Incluye el paquete donde está la entidad
@EnableJpaRepositories(basePackages = "com.habitask.repository") // Repositorios
@ComponentScan(basePackages = "com.habitask") // Otros componentes
public class CustomConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
