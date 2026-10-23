
package com.asignador.academico.repository;

import com.asignador.academico.entity.FranjaHoraria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FranjaHorariaRepository
        extends JpaRepository<FranjaHoraria, Long> {

    List<FranjaHoraria> findByDiaSemanaIgnoreCase(String diaSemana);

    List<FranjaHoraria> findByActivaTrue();
}
