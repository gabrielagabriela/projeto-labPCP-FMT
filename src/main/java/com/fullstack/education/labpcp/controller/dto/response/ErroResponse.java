package com.fullstack.education.labpcp.controller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErroResponse {

    private String codigo;
    private String mensagem;
}
