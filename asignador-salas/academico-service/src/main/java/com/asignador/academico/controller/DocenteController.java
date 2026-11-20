package com.asignador.academico.controller;

import com.asignador.academico.entity.Docente;
import com.asignador.academico.service.DocenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {
    private final DocenteService service;
    public DocenteController(DocenteService service) { this.service = service; }

    @GetMapping
    public List<Docente> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Docente buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<Docente> crear(@RequestBody Docente d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(d));
    }

    @PutMapping("/{id}")
    public Docente actualizar(@PathVariable Long id, @RequestBody Docente d) {
        return service.actualizar(id, d);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
