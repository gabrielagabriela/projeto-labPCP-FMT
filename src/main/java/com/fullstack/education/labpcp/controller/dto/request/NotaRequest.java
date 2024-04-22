package com.fullstack.education.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record NotaRequest(
        String nomeAluno,
        String nomeProfessor,
        String nomeMateria,
        double valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data
) {
}
