package com.asignador.academico.service;

import com.asignador.academico.entity.Docente;
import com.asignador.academico.repository.DocenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DocenteService {
    private final DocenteRepository repo;
    public DocenteService(DocenteRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<Docente> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Docente buscar(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No existe el docente con ID: " + id));
    }

    public Docente crear(Docente d) {
        validar(d);
        d.setNombres(d.getNombres().trim());
        d.setApellidos(d.getApellidos().trim());
        d.setCorreo(d.getCorreo().trim());
        if (repo.existsByCorreoIgnoreCase(d.getCorreo()))
            throw new IllegalArgumentException("Ya existe un docente con ese correo");
        return repo.save(d);
    }

    public Docente actualizar(Long id, Docente datos) {
        Docente d = buscar(id);
        validar(datos);
        String correo = datos.getCorreo().trim();
        repo.findByCorreoIgnoreCase(correo).filter(x -> !x.getId().equals(id))
            .ifPresent(x -> { throw new IllegalArgumentException("Ya existe un docente con ese correo"); });
        d.setNombres(datos.getNombres().trim());
        d.setApellidos(datos.getApellidos().trim());
        d.setCorreo(correo);
        d.setActivo(datos.isActivo());
        return repo.save(d);
    }

    public void eliminar(Long id) { repo.delete(buscar(id)); }

    private void validar(Docente d) {
        if (d == null || d.getNombres() == null || d.getNombres().isBlank()
            || d.getApellidos() == null || d.getApellidos().isBlank()
            || d.getCorreo() == null || d.getCorreo().isBlank())
            throw new IllegalArgumentException("Nombres, apellidos y correo son obligatorios");
    }
}
