package com.asignador.academico.repository;

import com.asignador.academico.entity.DocentePrograma;
import com.asignador.academico.entity.DocenteProgramaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocenteProgramaRepository
        extends JpaRepository<DocentePrograma, DocenteProgramaId> {

    List<DocentePrograma> findByDocenteId(Long docenteId);

    List<DocentePrograma> findByProgramaId(Long programaId);

    boolean existsByDocenteIdAndProgramaId(
        Long docenteId,
        Long programaId
    );
}