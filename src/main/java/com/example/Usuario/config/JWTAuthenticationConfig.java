package com.example.Usuario.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTAuthenticationConfig {

    // Clave secreta temporal para que compile (Al menos 32 caracteres)
    private static final String SUPER_SECRET_KEY = "EstaEsUnaClaveSecretaMuyLargaYSeguraParaMifirmaDeTokensJWT2026";
    private static final long TOKEN_EXPIRATION_TIME = 3600000; // 1 hora

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SUPER_SECRET_KEY.getBytes());
    }

    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
}