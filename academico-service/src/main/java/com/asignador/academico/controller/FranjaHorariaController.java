package com.asignador.academico.controller;

import com.asignador.academico.entity.FranjaHoraria;
import com.asignador.academico.service.FranjaHorariaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/franjas-horarias")
public class FranjaHorariaController {
    private final FranjaHorariaService service;
    public FranjaHorariaController(FranjaHorariaService service) { this.service = service; }

    @GetMapping
    public List<FranjaHoraria> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public FranjaHoraria buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<FranjaHoraria> crear(@RequestBody FranjaHoraria f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(f));
    }

    @PutMapping("/{id}")
    public FranjaHoraria actualizar(@PathVariable Long id, @RequestBody FranjaHoraria f) {
        return service.actualizar(id, f);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
