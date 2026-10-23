package com.asignador.academico.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DocenteProgramaId implements Serializable {

    @Column(name = "docente_id")
    private Long docenteId;

    @Column(name = "programa_id")
    private Long programaId;

    public DocenteProgramaId() {
    }

    public DocenteProgramaId(Long docenteId, Long programaId) {
        this.docenteId = docenteId;
        this.programaId = programaId;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof DocenteProgramaId otro)) {
            return false;
        }

        return Objects.equals(docenteId, otro.docenteId)
                && Objects.equals(programaId, otro.programaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docenteId, programaId);
    }
}