
package com.asignador.academico.repository;

import com.asignador.academico.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    Optional<Programa> findByNombreIgnoreCase(String nombre);

    boolean existsByCodigoIgnoreCase(String codigo);
}
