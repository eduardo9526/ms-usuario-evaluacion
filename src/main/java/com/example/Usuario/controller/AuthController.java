package com.example.Usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Usuario.config.JWTAuthenticationConfig;
import com.example.Usuario.model.entitie.Usuario;
import com.example.Usuario.model.request.AgregarUsuario;
import com.example.Usuario.model.request.LoginRequest;
import com.example.Usuario.service.UsuarioService;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. ENDPOINT DE REGISTRO
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody AgregarUsuario agregarUsuario) {
        // Encriptamos la contraseña plana antes de mandarla al flujo del servicio
        String passwordEncriptada = passwordEncoder.encode(agregarUsuario.password());
        
        // Creamos un nuevo objeto con la contraseña modificada de forma segura
        AgregarUsuario usuarioConClaveSegura = new AgregarUsuario(
                agregarUsuario.nombre(),
                agregarUsuario.email(),
                passwordEncriptada,
                agregarUsuario.rol()
        );
        
        Usuario usuarioGuardado = usuarioService.agregarUsuario(usuarioConClaveSegura);
        return ResponseEntity.ok(usuarioGuardado);
    }

    // 2. ENDPOINT DE LOGIN (Genera el JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Spring Security se encarga de validar el correo y la contraseña contra la BD
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        // Si la autenticación fue exitosa, buscamos los datos para meter el Rol en el Token
        // Nota: cargamos el rol directamente desde el objeto principal que autenticó Spring
        String email = authentication.getName();
        
        // Para simplificar y no romper tu arquitectura, obtenemos el rol del contexto de autoridades de Spring
        String rol = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        // Generamos el preciado token JWT
        String token = jwtAuthenticationConfig.generateToken(email, rol);

        // Devolvemos una respuesta limpia con el token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }
}