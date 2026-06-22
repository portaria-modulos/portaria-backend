package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiSwageer {
    @Bean
    public OpenAPI Validation(){
        return  new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                        )
                ).info(new Info()
                        .title("Registro de Portaria")
                        .description("Sistema de Registro de portaria")
                        .contact(new Contact()
                                .email("grupomateus@gmail.com\n")
                                .name("Portaria")
                        )
                        .license(new License()
                                .name("apache 2.0")
                                .url("http://localhost:8080")
                        )
                );

    }
}
