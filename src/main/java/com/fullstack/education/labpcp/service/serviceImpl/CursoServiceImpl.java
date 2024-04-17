package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.CursoRequest;
import com.fullstack.education.labpcp.controller.dto.response.CursoResponse;
import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import com.fullstack.education.labpcp.datasource.repository.CursoRepository;
import com.fullstack.education.labpcp.service.CursoService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final TokenService tokenService;

    public void papelUsuarioAcessoPermitido(String token){

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")){
            throw new RuntimeException("Apenas administradores e pedagogos podem acessar os endpoints de curso!");
        }
    }

    @Override
    public CursoResponse criarCurso(CursoRequest cursoRequest, String token) {

        papelUsuarioAcessoPermitido(token);
        CursoEntity curso = new CursoEntity();
        curso.setNome(cursoRequest.nome());
        cursoRepository.save(curso);
        return new CursoResponse(curso.getId(), curso.getNome());
    }

    public CursoEntity cursoPorId(Long id){
        CursoEntity cursoPesquisado = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhum curso cadastrado"));
        return cursoPesquisado;
    }
    @Override
    public CursoResponse obterCursoPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        CursoEntity cursoPesquisado = cursoPorId(id);
        return new CursoResponse(cursoPesquisado.getId(),cursoPesquisado.getNome());
    }

    @Override
    public CursoResponse atualizarCurso(Long id, CursoRequest cursoRequest, String token) {
        papelUsuarioAcessoPermitido(token);
        CursoEntity cursoPesquisado = cursoPorId(id);
        cursoPesquisado.setId(id);
        cursoPesquisado.setNome(cursoRequest.nome());
        cursoRepository.save(cursoPesquisado);
        return new CursoResponse(cursoPesquisado.getId(),cursoPesquisado.getNome());
    }

    @Override
    public void excluirCurso(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem podem excluir curso!");
        }

        CursoEntity cursoPesquisado = cursoPorId(id);
        cursoRepository.delete(cursoPesquisado);

    }

    @Override
    public List<CursoResponse> listarTodosCursos(String token) {
        papelUsuarioAcessoPermitido(token);
        return cursoRepository.findAll().stream().map(
                d -> new CursoResponse(d.getId(), d.getNome())
        ).toList();
    }
}
