package com.example.Usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Usuario.model.entitie.Usuario;
import com.example.Usuario.repository.UsuarioRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos el usuario por el email en tu base de datos
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Retornamos el objeto UserDetails que Spring Security necesita para validar la sesión
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // Esta clave se comparará internamente
                .roles(usuario.getRol().toUpperCase()) // Spring le añade "ROLE_" de forma automática
                .build();
    }
}