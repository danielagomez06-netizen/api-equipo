package com.asignador.academico.controller;

import com.asignador.academico.entity.Sala;
import com.asignador.academico.service.SalaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalaController {
    private final SalaService service;
    public SalaController(SalaService service) { this.service = service; }

    @GetMapping
    public List<Sala> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Sala buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<Sala> crear(@RequestBody Sala sala) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(sala));
    }

    @PutMapping("/{id}")
    public Sala actualizar(@PathVariable Long id, @RequestBody Sala sala) {
        return service.actualizar(id, sala);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
