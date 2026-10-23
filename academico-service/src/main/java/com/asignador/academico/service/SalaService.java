package com.asignador.academico.service;

import com.asignador.academico.entity.Sala;
import com.asignador.academico.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SalaService {
    private final SalaRepository repo;

    public SalaService(SalaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Sala> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Sala buscar(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No existe la sala con ID: " + id));
    }

    public Sala crear(Sala sala) {
        validar(sala);
        if (repo.existsByNombreIgnoreCase(sala.getNombre().trim())) {
            throw new IllegalArgumentException("Ya existe una sala con ese nombre");
        }
        sala.setNombre(sala.getNombre().trim());
        return repo.save(sala);
    }

    public Sala actualizar(Long id, Sala datos) {
        Sala sala = buscar(id);
        validar(datos);
        repo.findByNombreIgnoreCase(datos.getNombre().trim())
            .filter(s -> !s.getId().equals(id))
            .ifPresent(s -> { throw new IllegalArgumentException("Ya existe una sala con ese nombre"); });
        sala.setNombre(datos.getNombre().trim());
        sala.setCapacidad(datos.getCapacidad());
        sala.setUbicacion(datos.getUbicacion());
        sala.setActiva(datos.isActiva());
        return repo.save(sala);
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private void validar(Sala sala) {
        if (sala == null || sala.getNombre() == null || sala.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la sala es obligatorio");
        if (sala.getCapacidad() <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
    }
}
