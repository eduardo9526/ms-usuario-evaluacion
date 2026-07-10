package com.example.Usuario.model.request;

import jakarta.validation.constraints.NotBlank;

public record AgregarUsuario(
    @NotBlank(message = "El nombre no puede estar vacío") String nombre,
    @NotBlank String email,
    @NotBlank String password,
    @NotBlank String rol
) {}