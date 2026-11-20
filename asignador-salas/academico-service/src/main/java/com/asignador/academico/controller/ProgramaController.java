
package com.asignador.academico.controller;

import com.asignador.academico.entity.Programa;
import com.asignador.academico.service.ProgramaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    private final ProgramaService programaService;

    public ProgramaController(ProgramaService programaService) {
        this.programaService = programaService;
    }

    @GetMapping
    public List<Programa> listarTodos() {
        return programaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Programa buscarPorId(@PathVariable Long id) {
        return programaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Programa> crear(@RequestBody Programa programa) {
        Programa creado = programaService.crear(programa);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public Programa actualizar(
            @PathVariable Long id,
            @RequestBody Programa programa) {
        return programaService.actualizar(id, programa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

