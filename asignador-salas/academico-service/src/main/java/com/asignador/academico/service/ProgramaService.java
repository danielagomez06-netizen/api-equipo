
package com.asignador.academico.service;

import com.asignador.academico.entity.Programa;
import com.asignador.academico.repository.ProgramaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProgramaService {

    private final ProgramaRepository programaRepository;

    public ProgramaService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    @Transactional(readOnly = true)
    public List<Programa> listarTodos() {
        return programaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Programa buscarPorId(Long id) {
        return programaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro el programa con ID: " + id));
    }

    public Programa crear(Programa programa) {
        validarDatos(programa);
        validarDuplicados(programa, null);
        return programaRepository.save(programa);
    }

    public Programa actualizar(Long id, Programa datos) {
        Programa existente = buscarPorId(id);

        validarDatos(datos);
        validarDuplicados(datos, id);

        existente.setNombre(datos.getNombre().trim());
        existente.setCodigo(datos.getCodigo().trim());
        existente.setActivo(datos.isActivo());

        return programaRepository.save(existente);
    }

    public void eliminar(Long id) {
        Programa programa = buscarPorId(id);
        programaRepository.delete(programa);
    }

    private void validarDatos(Programa programa) {
        if (programa == null) {
            throw new IllegalArgumentException(
                    "Los datos del programa son obligatorios");
        }

        if (programa.getNombre() == null
                || programa.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del programa es obligatorio");
        }

        if (programa.getCodigo() == null
                || programa.getCodigo().isBlank()) {
            throw new IllegalArgumentException(
                    "El codigo del programa es obligatorio");
        }

        programa.setNombre(programa.getNombre().trim());
        programa.setCodigo(programa.getCodigo().trim());
    }

    private void validarDuplicados(Programa programa, Long idExcluir) {
        programaRepository.findByNombreIgnoreCase(programa.getNombre())
                .filter(p -> !p.getId().equals(idExcluir))
                .ifPresent(p -> {
                    throw new IllegalArgumentException(
                            "Ya existe un programa con ese nombre");
                });

        boolean codigoDuplicado = programaRepository
                .existsByCodigoIgnoreCase(programa.getCodigo());

        if (codigoDuplicado) {
            boolean perteneceAOtro = programaRepository.findAll().stream()
                    .anyMatch(p ->
                            p.getCodigo().equalsIgnoreCase(programa.getCodigo())
                                    && !p.getId().equals(idExcluir));

            if (perteneceAOtro) {
                throw new IllegalArgumentException(
                        "Ya existe un programa con ese codigo");
            }
        }
    }
}
