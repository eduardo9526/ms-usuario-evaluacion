package com.example.Usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar anotaciones como @PreAuthorize si quieres validar roles directo en los métodos
public class WebSecurityConfig {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    // Inyectamos el filtro que creamos en el paso anterior
    public WebSecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    // Bean para encriptar contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Administrador de autenticación nativo de Spring
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuración principal de las reglas de seguridad HTTP
    // Configuración principal de las reglas de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF ya que los tokens JWT son inmunes a este ataque sin cookies
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Arquitectura sin estado (Stateless)
            .authorizeHttpRequests(auth -> auth
                // 1. Agregamos tus controladores reales (/autenticación y /usuarios para POST de registro)
                .requestMatchers("/autenticación/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/usuarios").permitAll() // Deja crear usuarios libremente
                
                // 2. Corregimos las rutas de Swagger y la documentación de OpenAPI
                .requestMatchers("/v3/api-docs/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Cualquier otra petición (como los GET o DELETE de usuarios) requerirá obligatoriamente el JWT válido
                .anyRequest().authenticated()
            )
            // Enganchamos nuestro filtro aduanero antes del filtro de autenticación por defecto de Spring
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}