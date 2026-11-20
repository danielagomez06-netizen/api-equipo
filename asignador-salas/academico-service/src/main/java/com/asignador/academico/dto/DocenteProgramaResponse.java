package com.asignador.academico.dto;

public record DocenteProgramaResponse(
    Long docenteId,
    String docenteNombres,
    String docenteApellidos,
    Long programaId,
    String programaNombre
) {}