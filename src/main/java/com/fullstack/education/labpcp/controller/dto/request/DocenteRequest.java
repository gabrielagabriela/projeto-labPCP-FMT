package com.fullstack.education.labpcp.controller.dto.request;

import java.time.LocalDate;

public record DocenteRequest(
        String nome,
        LocalDate data_entrada,
        String login
) {
}
