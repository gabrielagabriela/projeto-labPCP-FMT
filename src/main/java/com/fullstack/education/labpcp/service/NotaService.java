package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.NotaRequest;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface NotaService {

    NotaResponse criarNota(@RequestBody NotaRequest notaRequest, String token);
    NotaResponse obterNotaPorId(Long id, String token);
    NotaResponse atualizarNota(Long id, @RequestBody NotaRequest notaRequest, String token);
    void excluirNota(Long id, String token);
    List<NotaResponse> listarTodasNotas(String token);
}

