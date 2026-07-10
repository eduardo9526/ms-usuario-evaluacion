package com.example.Usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient marcaWebClient() {
        return WebClient.builder().baseUrl("http://gestiondireccion.us-east-1.elasticbeanstalk.com").build();
    }

}
