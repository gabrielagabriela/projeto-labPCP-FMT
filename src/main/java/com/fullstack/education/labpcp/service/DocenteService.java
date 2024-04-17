package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.DocenteRequest;
import com.fullstack.education.labpcp.controller.dto.response.DocenteResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DocenteService {
    DocenteResponse criarDocente(@RequestBody DocenteRequest docenteRequest, String token);
    DocenteResponse obterDocentePorId(Long id, String token);
    DocenteResponse atualizarDocente(Long id,@RequestBody DocenteRequest docenteRequest, String token);
    void excluirDocente(Long id, String token);
    List<DocenteResponse> listarTodosDocentes(String token);
}
