package com.fullstack.education.labpcp.controller.dto.request;

public record CadastroUsuarioRequest(
        String login,
        String senha,
        String papel
) {
}


