package com.example.Usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Usuario.model.entitie.Usuario;
import com.example.Usuario.model.request.ActualizarUsuario;
import com.example.Usuario.model.request.AgregarUsuario;
import com.example.Usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

   
    // LISTAR TODOS LOS USUARIO
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // OBTENER USUARIO POR ID
    public Usuario obtenerUsuarioPorID(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehiculo no encontrado.");
        }
        return usuario;
    }

    public Usuario agregarUsuario(AgregarUsuario nuevoUsuario) {
    Usuario usuario = new Usuario();
    // En los Records se accede sin el "get"
    usuario.setNombre(nuevoUsuario.nombre());   
    usuario.setEmail(nuevoUsuario.email());
    usuario.setPassword(nuevoUsuario.password());
    usuario.setRol(nuevoUsuario.rol());
    
    return usuarioRepository.save(usuario);
}

// ACTUALIZAR USUARIO
public Usuario actualizarUsuario(ActualizarUsuario datos, int idUsuario) {
    Usuario existente = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    existente.setNombre(datos.nombre());
    existente.setEmail(datos.email());
    existente.setPassword(datos.password());
    existente.setRol(datos.rol());

    return usuarioRepository.save(existente);
}

    // ELIMINAR USUARIO POR ID
    public String eliminarUsuario(int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            usuarioRepository.deleteById(idUsuario);
            return "Usuario eliminado correctamente";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "ENDPOINT eliminarUsuario no ha encontrado la id_direccion ingresado(IF).");
        }
    }

}
