package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.TurmaRequest;
import com.fullstack.education.labpcp.controller.dto.response.TurmaResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TurmaService {

    TurmaResponse criarTurma(@RequestBody TurmaRequest turmaRequest, String token);

    TurmaResponse obterTurmaPorId(Long id, String token);

    TurmaResponse atualizarTurma(Long id, @RequestBody TurmaRequest turmaRequest, String token);

    void excluirTurma(Long id, String token);

    List<TurmaResponse> listarTodosTurmas(String token);
}

