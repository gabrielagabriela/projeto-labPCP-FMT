package com.fullstack.education.labpcp.controller.dto.request;

import java.time.LocalDate;

public record NotaRequest(
        String nomeAluno,
        String nomeProfessor,
        String nomeMateria,
        double valor,
        LocalDate data
) {
}
