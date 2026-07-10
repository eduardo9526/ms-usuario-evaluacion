package com.example.Usuario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {

    @Value("${app.version}")
    private String version;

    @Bean
    public OpenAPI OpenApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestion de Usuarios - Microservicio.")
                        .version(version)
                        .description("C.R.U.D. y buscador de Usuarios.")
                        .contact(new Contact()
                                .email("Edutech@duocuc.cl")
                                .name("EdutechDev.DuocUC")
                                .url("google.cl")));
    }

}
