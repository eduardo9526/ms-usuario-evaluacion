package com.example.Usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Usuario.model.entitie.Usuario;
import com.example.Usuario.model.request.ActualizarUsuario;
import com.example.Usuario.model.request.AgregarUsuario;
import com.example.Usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/{idUsuario}")
    public Usuario obtenerUsuarioPorID(@PathVariable int idUsuario) {
        return usuarioService.obtenerUsuarioPorID(idUsuario);
    }

    @PostMapping("")
    public Usuario agregarUsuario(@Valid @RequestBody AgregarUsuario agregarUsuario) {
        return usuarioService.agregarUsuario(agregarUsuario);
    }

    @PutMapping("/{idUsuario}")
    public Usuario actualizarUsuario(@PathVariable int idUsuario,
            @Valid @RequestBody ActualizarUsuario actualizarUsuario) {
        return usuarioService.actualizarUsuario(actualizarUsuario, idUsuario);
    }

    @DeleteMapping("/{idUsuario}")
    public String eliminarUsuario(@PathVariable int idUsuario) {
        return usuarioService.eliminarUsuario(idUsuario);
    }

}
