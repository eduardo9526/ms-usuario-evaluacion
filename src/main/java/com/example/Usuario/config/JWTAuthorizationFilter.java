package com.example.Usuario.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    // Usamos la misma clave secreta temporal que pusimos en el generador
    private static final String SUPER_SECRET_KEY = "EstaEsUnaClaveSecretaMuyLargaYSeguraParaMifirmaDeTokensJWT2026";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SUPER_SECRET_KEY.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Extraemos el encabezado "Authorization" de la petición HTTP
        String authHeader = request.getHeader("Authorization");

        // 2. Verificamos si el encabezado existe y comienza con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            
            try {
                // 3. Parseamos y validamos el token JWT con nuestra clave secreta
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String username = claims.getSubject();
                String rol = claims.get("rol", String.class);

                if (username != null) {
                    // 4. Mapeamos el rol anteponiendo el prefijo "ROLE_" que exige Spring Security internamente
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase())
                    );

                    // 5. Creamos el objeto de autenticación con el usuario y sus roles
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, authorities
                    );
                    
                    // 6. Seteamos la autenticación en el contexto de seguridad de Spring para esta petición
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // Si el token expiró, fue manipulado o es inválido, limpiamos cualquier acceso
                SecurityContextHolder.clearContext();
            }
        }

        // 7. Continuamos con el flujo normal de la petición HTTP
        filterChain.doFilter(request, response);
    }
}