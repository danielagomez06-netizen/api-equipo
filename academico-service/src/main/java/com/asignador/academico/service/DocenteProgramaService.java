package com.asignador.academico.service;

import com.asignador.academico.dto.DocenteProgramaResponse;
import com.asignador.academico.entity.Docente;
import com.asignador.academico.entity.DocentePrograma;
import com.asignador.academico.entity.DocenteProgramaId;
import com.asignador.academico.entity.Programa;
import com.asignador.academico.repository.DocenteProgramaRepository;
import com.asignador.academico.repository.DocenteRepository;
import com.asignador.academico.repository.ProgramaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocenteProgramaService {

    private final DocenteProgramaRepository relacionRepository;
    private final DocenteRepository docenteRepository;
    private final ProgramaRepository programaRepository;

    public DocenteProgramaService(
            DocenteProgramaRepository relacionRepository,
            DocenteRepository docenteRepository,
            ProgramaRepository programaRepository) {
        this.relacionRepository = relacionRepository;
        this.docenteRepository = docenteRepository;
        this.programaRepository = programaRepository;
    }

    private DocenteProgramaResponse convertir(DocentePrograma relacion) {
        Docente docente = relacion.getDocente();
        Programa programa = relacion.getPrograma();

        return new DocenteProgramaResponse(
            docente.getId(),
            docente.getNombres(),
            docente.getApellidos(),
            programa.getId(),
            programa.getNombre()
        );
    }

    @Transactional(readOnly = true)
    public List<DocenteProgramaResponse> listarTodos() {
        return relacionRepository.findAll()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DocenteProgramaResponse> listarPorDocente(Long docenteId) {
        if (!docenteRepository.existsById(docenteId)) {
            throw new IllegalArgumentException("El docente indicado no existe.");
        }

        return relacionRepository.findByDocenteId(docenteId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DocenteProgramaResponse> listarPorPrograma(Long programaId) {
        if (!programaRepository.existsById(programaId)) {
            throw new IllegalArgumentException("El programa indicado no existe.");
        }

        return relacionRepository.findByProgramaId(programaId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional
    public DocenteProgramaResponse asociar(Long docenteId, Long programaId) {
        Docente docente = docenteRepository.findById(docenteId)
            .orElseThrow(() ->
                new IllegalArgumentException("El docente indicado no existe."));

        Programa programa = programaRepository.findById(programaId)
            .orElseThrow(() ->
                new IllegalArgumentException("El programa indicado no existe."));

        if (relacionRepository.existsByDocenteIdAndProgramaId(docenteId, programaId)) {
            throw new IllegalArgumentException(
                "El docente ya está asociado a ese programa.");
        }

        DocentePrograma relacion =
            relacionRepository.save(new DocentePrograma(docente, programa));

        return convertir(relacion);
    }

    @Transactional
    public void eliminar(Long docenteId, Long programaId) {
        DocenteProgramaId id = new DocenteProgramaId(docenteId, programaId);

        DocentePrograma relacion = relacionRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("La asociación indicada no existe."));

        relacionRepository.delete(relacion);
    }
}