package com.asignador.academico.service;

import com.asignador.academico.entity.FranjaHoraria;
import com.asignador.academico.repository.FranjaHorariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class FranjaHorariaService {
    private final FranjaHorariaRepository repo;
    public FranjaHorariaService(FranjaHorariaRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<FranjaHoraria> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public FranjaHoraria buscar(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No existe la franja con ID: " + id));
    }

    public FranjaHoraria crear(FranjaHoraria f) {
        validar(f);
        return repo.save(f);
    }

    public FranjaHoraria actualizar(Long id, FranjaHoraria datos) {
        FranjaHoraria f = buscar(id);
        validar(datos);
        f.setDiaSemana(datos.getDiaSemana());
        f.setHoraInicio(datos.getHoraInicio());
        f.setHoraFin(datos.getHoraFin());
        f.setActiva(datos.isActiva());
        return repo.save(f);
    }

    public void eliminar(Long id) { repo.delete(buscar(id)); }

    private void validar(FranjaHoraria f) {
        if (f == null || f.getDiaSemana() == null || f.getDiaSemana().isBlank()
            || f.getHoraInicio() == null || f.getHoraFin() == null)
            throw new IllegalArgumentException("Dia y horas de la franja son obligatorios");
        if (!f.getHoraFin().isAfter(f.getHoraInicio()))
            throw new IllegalArgumentException("La hora final debe ser posterior a la inicial");
    }
}
