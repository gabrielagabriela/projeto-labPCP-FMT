package com.fullstack.education.labpcp.service;

import com.fullstack.education.labpcp.controller.dto.request.MateriaRequest;
import com.fullstack.education.labpcp.controller.dto.response.MateriaResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MateriaService {

    MateriaResponse criarMateria(@RequestBody MateriaRequest materiaRequest, String token);
    MateriaResponse obterMateriaPorId(Long id, String token);
    MateriaResponse atualizarMateria(Long id, @RequestBody MateriaRequest materiaRequest, String token);
    void excluirMateria(Long id, String token);
    List<MateriaResponse> listarTodasMaterias(String token);
}

