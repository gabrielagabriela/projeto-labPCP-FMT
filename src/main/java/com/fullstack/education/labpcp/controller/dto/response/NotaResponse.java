package com.fullstack.education.labpcp.controller.dto.response;

import java.time.LocalDate;

public record NotaResponse(
        Long id_nota,
        String nomeAluno,
        String nomeProfessor,
        String nomeMateria,
        double valor,
        LocalDate data
) {
}

