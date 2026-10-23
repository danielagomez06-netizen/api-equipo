package com.asignador.academico.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "docente_programa")
public class DocentePrograma {

    @EmbeddedId
    private DocenteProgramaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("docenteId")
    @JoinColumn(name = "docente_id")
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("programaId")
    @JoinColumn(name = "programa_id")
    private Programa programa;

    public DocentePrograma() {
    }

    public DocentePrograma(Docente docente, Programa programa) {
        this.docente = docente;
        this.programa = programa;
        this.id = new DocenteProgramaId(
            docente.getId(),
            programa.getId()
        );
    }

    public DocenteProgramaId getId() {
        return id;
    }

    public void setId(DocenteProgramaId id) {
        this.id = id;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
}