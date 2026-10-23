package com.asignador.academico.controller;

import com.asignador.academico.dto.DocenteProgramaResponse;
import com.asignador.academico.service.DocenteProgramaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/docente-programas")
public class DocenteProgramaController {

    private final DocenteProgramaService service;

    public DocenteProgramaController(DocenteProgramaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DocenteProgramaResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/docente/{docenteId}")
    public List<DocenteProgramaResponse> listarPorDocente(
            @PathVariable Long docenteId) {
        return service.listarPorDocente(docenteId);
    }

    @GetMapping("/programa/{programaId}")
    public List<DocenteProgramaResponse> listarPorPrograma(
            @PathVariable Long programaId) {
        return service.listarPorPrograma(programaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocenteProgramaResponse asociar(@RequestBody Map<String, Long> datos) {
        Long docenteId = datos.get("docenteId");
        Long programaId = datos.get("programaId");

        if (docenteId == null || programaId == null) {
            throw new IllegalArgumentException(
                "Debes proporcionar docenteId y programaId.");
        }

        return service.asociar(docenteId, programaId);
    }

    @DeleteMapping("/{docenteId}/{programaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long docenteId,
            @PathVariable Long programaId) {
        service.eliminar(docenteId, programaId);
    }
}