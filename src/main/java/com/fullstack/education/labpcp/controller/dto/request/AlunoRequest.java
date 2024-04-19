package com.fullstack.education.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AlunoRequest(
        String nome,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_nascimento,
        String login,
        String nomeTurma

) {
}
