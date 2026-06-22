package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfigure implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/portaria/v1/avatar/**")
                .addResourceLocations("file:avatar/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));
        registry.addResourceHandler("/portaria/v1/entrada/**")
                .addResourceLocations("file:entrada/")
                .setCacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES));
        registry.addResourceHandler("/portaria/v1/saida/**")
                .addResourceLocations("file:saida/")
                .setCacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES));
        registry.addResourceHandler("/portaria/v1/usuario/**")
                .addResourceLocations("file:usuario/")
                .setCacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES));

    }
}
