package com.equipo.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipo")
public class EquipoController {

    // Endpoint base — no modificar
    @GetMapping
    public List<String> integrantes() {
        return List.of("Equipo listo. Cada integrante agrega su endpoint abajo.");
    }

    // ─────────────────────────────────────────────────────────────────
    // ZONA DE TRABAJO DEL EQUIPO
    // ─────────────────────────────────────────────────────────────────
     @GetMapping("Daniela Gutierrez")
    public String saludo() {
        return "Hola, soy Daniela Gutierrez y este es mi endpoint.";
    }

}
