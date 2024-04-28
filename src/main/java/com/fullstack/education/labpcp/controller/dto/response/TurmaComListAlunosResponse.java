package com.fullstack.education.labpcp.controller.dto.response;

import java.util.List;

public record TurmaComListAlunosResponse(
        Long id_turma,
        String nome,
        String nomeProfessor,
        String nomeCurso,
        List<String> alunos

) {
}
