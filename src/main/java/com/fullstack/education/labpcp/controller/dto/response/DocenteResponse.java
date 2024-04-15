package com.fullstack.education.labpcp.controller.dto.response;

import java.time.LocalDate;

public record DocenteResponse(
        Long id_docente,
        String nome,
        LocalDate data_entrada,
        String login
) {
}
