package com.fullstack.education.labpcp.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

public record DocenteRequest(
        String nome,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_entrada,
        String login
) {
}
