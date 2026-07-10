package com.example.Usuario.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Usuario.model.request.InfoVersion;

@RequestMapping("/")
public class InfoVersionController {

    @Value("${app.name}")
    private String nombre;

    @Value("${app.version}")
    private String version;

    @GetMapping("")
    public InfoVersion info() {
        return new InfoVersion(nombre, version);
    }
}
