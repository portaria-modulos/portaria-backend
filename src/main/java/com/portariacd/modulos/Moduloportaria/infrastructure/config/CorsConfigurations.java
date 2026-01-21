package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfigurations {
    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173",
                                "http://10.88.2.26:5173/",
                                "http://192.168.88.239:8085",
                                "https://192.168.100.91:8085",
                                "http://10.220.115.100/",
                                "http://192.168.88.239/",
                                "http://portaria.grupomateus/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders(
                                "Content-Type",
                                "Authorization",
                                "tokenkey",
                                "X-Requested-With",
                                "Accept",
                                "Origin",
                                "Access-Control-Request-Method",
                                "Access-Control-Request-Headers"
                        ).exposedHeaders("Authorization", "tokenkey")
                        .allowCredentials(true);
            }
        };
    }
}
