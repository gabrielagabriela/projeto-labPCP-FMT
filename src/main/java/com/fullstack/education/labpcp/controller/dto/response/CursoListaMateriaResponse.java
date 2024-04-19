package com.fullstack.education.labpcp.controller.dto.response;

import java.util.List;

public record CursoListaMateriaResponse(
        Long id_curso,
        String nome,
        List<String> materias
) {
}

