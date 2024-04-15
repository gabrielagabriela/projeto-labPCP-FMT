package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface UsuarioService {
    CadastroUsuarioResponse cadastrarUsuario(@RequestBody CadastroUsuarioRequest request, String token);

    void cadastrarUsuarioPreDefinido(String login, String senha, String papel);

    boolean existeUsuariosCadastrados();
}
