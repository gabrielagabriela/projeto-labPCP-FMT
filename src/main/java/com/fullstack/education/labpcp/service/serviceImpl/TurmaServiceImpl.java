package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.TurmaRequest;
import com.fullstack.education.labpcp.controller.dto.response.TurmaResponse;
import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import com.fullstack.education.labpcp.datasource.entity.TurmaEntity;
import com.fullstack.education.labpcp.datasource.repository.CursoRepository;
import com.fullstack.education.labpcp.datasource.repository.DocenteRepository;
import com.fullstack.education.labpcp.datasource.repository.TurmaRepository;
import com.fullstack.education.labpcp.service.TurmaService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TurmaServiceImpl implements TurmaService {

    private final TurmaRepository turmaRepository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;

    public void papelUsuarioAcessoPermitido(String token){


        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")){
            throw new RuntimeException("Apenas administradores e pedagogos podem acessar os endpoints de turma!");
        }
    }

    @Override
    public TurmaResponse criarTurma(TurmaRequest turmaRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        DocenteEntity docente = docenteRepository.findByNome(turmaRequest.nomeProfessor()).orElseThrow(() -> new RuntimeException("Este professor não existe ou ainda não está cadastrado como um docente"));

        boolean usuarioProfessor = docenteRepository.existsByNomeAndLoginPapelNome(turmaRequest.nomeProfessor(), "PROFESSOR");
        if(!usuarioProfessor){
            throw new RuntimeException("Este docente não tem papel de professor");
        }

        TurmaEntity turma = new TurmaEntity();
        turma.setNome(turmaRequest.nome());
        turma.setNomeProfessor(docente);
        turma.setNomeCurso(cursoRepository.findByNome(turmaRequest.nomeCurso()).orElseThrow(() -> new RuntimeException("Não há curso cadastrado com esse nome")));
        turmaRepository.save(turma);

        return new TurmaResponse(turma.getId(), turma.getNome(), turma.getNomeProfessor().getNome(), turma.getNomeCurso().getNome());
    }

    public TurmaEntity turmaPorId(Long id){
        TurmaEntity turmaPesquisada = turmaRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhuma turma cadastrada"));
        return turmaPesquisada;
    }
    @Override
    public TurmaResponse obterTurmaPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        TurmaEntity turmaPesquisada = turmaPorId(id);
        return new TurmaResponse(turmaPesquisada.getId(),turmaPesquisada.getNome(), turmaPesquisada.getNomeProfessor().getNome(), turmaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public TurmaResponse atualizarTurma(Long id, TurmaRequest turmaRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        TurmaEntity turmaPesquisada = turmaPorId(id);

        DocenteEntity docente = docenteRepository.findByNome(turmaRequest.nomeProfessor()).orElseThrow(() -> new RuntimeException("Este professor não existe ou ainda não está cadastrado como um docente"));

        boolean usuarioProfessor = docenteRepository.existsByNomeAndLoginPapelNome(turmaRequest.nomeProfessor(), "PROFESSOR");
        if(!usuarioProfessor){
            throw new RuntimeException("Este docente não tem papel de professor");
        }

        turmaPesquisada.setId(id);
        turmaPesquisada.setNome(turmaRequest.nome());
        turmaPesquisada.setNomeProfessor(docente);
        turmaPesquisada.setNomeCurso(cursoRepository.findByNome(turmaRequest.nomeCurso()).orElseThrow(() -> new RuntimeException("Não há curso cadastrado com esse nome")));

        turmaRepository.save(turmaPesquisada);

        return new TurmaResponse(turmaPesquisada.getId(), turmaPesquisada.getNome(), turmaPesquisada.getNomeProfessor().getNome(), turmaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public void excluirTurma(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem podem excluir turma!");
        }

        TurmaEntity turmaPesquisada = turmaPorId(id);
        turmaRepository.delete(turmaPesquisada);

    }

    @Override
    public List<TurmaResponse> listarTodosTurmas(String token) {
        papelUsuarioAcessoPermitido(token);

        return turmaRepository.findAll().stream().map(turma -> {
            String nomeProfessor = turma.getNomeProfessor() != null ? turma.getNomeProfessor().getNome() : null;
            String nomeCurso = turma.getNomeCurso() != null ? turma.getNomeCurso().getNome() : null;

            return new TurmaResponse(turma.getId(), turma.getNome(), nomeProfessor, nomeCurso);
        }).toList();
    }

}
