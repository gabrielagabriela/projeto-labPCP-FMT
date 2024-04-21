package com.fullstack.education.labpcp.controller.dto.response;

import java.time.LocalDate;

public record PontuacaoTotalAlunoResponse(
        Long id_aluno,
        String nome_aluno,
        Double pontuacao_total
) {
}
