package com.asignador.academico.service;

import com.asignador.academico.entity.Curso;
import com.asignador.academico.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CursoService {
    private final CursoRepository repo;
    public CursoService(CursoRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<Curso> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Curso buscar(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No existe el curso con ID: " + id));
    }

    public Curso crear(Curso c) {
        validar(c);
        return repo.save(c);
    }

    public Curso actualizar(Long id, Curso datos) {
        Curso c = buscar(id);
        validar(datos);
        c.setNombre(datos.getNombre().trim());
        c.setCodigo(datos.getCodigo().trim());
        c.setDocente(datos.getDocente());
        c.setPrograma(datos.getPrograma());
        c.setSemestre(datos.getSemestre());
        c.setCantidadEstudiantes(datos.getCantidadEstudiantes());
        c.setAsignable(datos.isAsignable());
        c.setMotivoNoAsignable(datos.getMotivoNoAsignable());
        c.setActivo(datos.isActivo());
        return repo.save(c);
    }

    public void eliminar(Long id) { repo.delete(buscar(id)); }

    private void validar(Curso c) {
        if (c == null || c.getNombre() == null || c.getNombre().isBlank()
            || c.getCodigo() == null || c.getCodigo().isBlank()
            || c.getPrograma() == null || c.getDocente() == null)
            throw new IllegalArgumentException("Nombre, codigo, programa y docente son obligatorios");
        if (c.getSemestre() <= 0)
            throw new IllegalArgumentException("El semestre debe ser mayor que cero");
        if (c.getCantidadEstudiantes() <= 0)
            throw new IllegalArgumentException("La cantidad de estudiantes debe ser mayor que cero");
        c.setNombre(c.getNombre().trim());
        c.setCodigo(c.getCodigo().trim());
    }
}
