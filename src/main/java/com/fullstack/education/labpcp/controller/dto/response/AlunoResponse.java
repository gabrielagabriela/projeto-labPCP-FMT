package com.fullstack.education.labpcp.controller.dto.response;

import java.time.LocalDate;

public record AlunoResponse(
        Long id_aluno,
        String nome,
        LocalDate data_nascimento,
        String login,
        String nomeTurma
) {
}
