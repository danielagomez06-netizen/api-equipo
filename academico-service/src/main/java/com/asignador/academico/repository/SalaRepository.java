
package com.asignador.academico.repository;

import com.asignador.academico.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
