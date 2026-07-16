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
   @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
        .authorizeHttpRequests(auth -> auth
            // 🚀 Permitir acceso libre a los endpoints de registro, login y documentación
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/usuarios").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // Cualquier otra petición requerirá un token JWT válido
            .anyRequest().authenticated()
        )
        // Agregamos tu filtro JWT corregido justo antes del filtro de usuario/contraseña estándar
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}