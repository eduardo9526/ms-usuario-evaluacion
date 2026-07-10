package com.example.Usuario.model.request;

import jakarta.validation.constraints.NotBlank;

public record ActualizarUsuario(
    @NotBlank String nombre,
    @NotBlank String email,
    @NotBlank String password,
    @NotBlank String rol
) {}