package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.AlunoRequest;
import com.fullstack.education.labpcp.controller.dto.response.AlunoResponse;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AlunoService {
    AlunoResponse criarAluno(@RequestBody AlunoRequest alunoRequest, String token);
    AlunoResponse obterAlunoPorId(Long id, String token);
    AlunoResponse atualizarAluno(Long id,@RequestBody AlunoRequest alunoRequest, String token);
    void excluirAluno(Long id, String token);
    List<AlunoResponse> listarTodosAlunos(String token);
    List<NotaResponse> listarNotasPorAluno (Long id, String token);
}
