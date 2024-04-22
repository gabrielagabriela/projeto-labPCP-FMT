package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.CursoRequest;
import com.fullstack.education.labpcp.controller.dto.response.CursoListaMateriaResponse;
import com.fullstack.education.labpcp.controller.dto.response.CursoResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CursoService {

    CursoResponse criarCurso(@RequestBody CursoRequest cursoRequest, String token);

    CursoResponse obterCursoPorId(Long id, String token);

    CursoResponse atualizarCurso(Long id, @RequestBody CursoRequest cursoRequest, String token);

    void excluirCurso(Long id, String token);

    List<CursoResponse> listarTodosCursos(String token);

    List<CursoListaMateriaResponse> listarMateriasPorCurso(Long id, String token);
}

