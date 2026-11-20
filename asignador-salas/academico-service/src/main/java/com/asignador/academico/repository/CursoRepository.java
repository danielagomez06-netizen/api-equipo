
package com.asignador.academico.repository;

import com.asignador.academico.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);

    List<Curso> findByProgramaId(Long programaId);

    List<Curso> findByDocenteId(Long docenteId);

    List<Curso> findByAsignableTrue();

    List<Curso> findByActivoTrue();
}
