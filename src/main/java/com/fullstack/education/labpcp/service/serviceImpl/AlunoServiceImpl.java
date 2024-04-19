package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.AlunoRequest;
import com.fullstack.education.labpcp.controller.dto.response.AlunoResponse;
import com.fullstack.education.labpcp.datasource.entity.AlunoEntity;
import com.fullstack.education.labpcp.datasource.repository.AlunoRepository;
import com.fullstack.education.labpcp.datasource.repository.TurmaRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.service.AlunoService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService {


    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;

    public void papelUsuarioAcessoPermitido(String token){

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")){
            throw new RuntimeException("Apenas administradores e pedagogos podem acessar os endpoints de aluno!");
        }
    }

    @Override
    public AlunoResponse criarAluno(AlunoRequest alunoRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(alunoRequest.login(), "ALUNO");
        if(!usuarioAluno){
            throw new RuntimeException("Este usuário não tem papel de aluno");
        }

        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome(alunoRequest.nome());
        aluno.setData_nascimento(alunoRequest.data_nascimento());
        aluno.setLogin(usuarioRepository.findByLogin(alunoRequest.login()).orElseThrow(() -> new RuntimeException("Login inválido")));
        aluno.setNomeTurma(turmaRepository.findByNome(alunoRequest.nomeTurma()).orElseThrow(() -> new RuntimeException("Não há turma cadastrada com esse nome!")));

        alunoRepository.save(aluno);

        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getData_nascimento(), aluno.getLogin().getLogin(), aluno.getNomeTurma().getNome());

    }


    public AlunoEntity alunoPorId(Long id){
        AlunoEntity alunoPesquisado = alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhum aluno cadastrado"));
        return alunoPesquisado;
    }
    @Override
    public AlunoResponse obterAlunoPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        AlunoEntity alunoPesquisado = alunoPorId(id);
        return new AlunoResponse(alunoPesquisado.getId(), alunoPesquisado.getNome(),alunoPesquisado.getData_nascimento(), alunoPesquisado.getLogin().getLogin(), alunoPesquisado.getNomeTurma().getNome());
    }

    @Override
    public AlunoResponse atualizarAluno(Long id, AlunoRequest alunoRequest, String token) {
        papelUsuarioAcessoPermitido(token);
        AlunoEntity alunoPesquisado = alunoPorId(id);

        alunoPesquisado.setId(id);
        alunoPesquisado.setNome(alunoRequest.nome());
        alunoPesquisado.setData_nascimento(alunoRequest.data_nascimento());
        alunoPesquisado.setLogin(alunoPesquisado.getLogin());
        alunoPesquisado.setNomeTurma(alunoPesquisado.getNomeTurma());

        alunoRepository.save(alunoPesquisado);

        return new AlunoResponse(alunoPesquisado.getId(), alunoPesquisado.getNome(),alunoPesquisado.getData_nascimento(), alunoPesquisado.getLogin().getLogin(), alunoPesquisado.getNomeTurma().getNome());
    }

    @Override
    public void excluirAluno(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");
        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem excluir aluno!");
        }

        AlunoEntity alunoPesquisado = alunoPorId(id);
        alunoRepository.delete(alunoPesquisado);

    }

    @Override
    public List<AlunoResponse> listarTodosAlunos(String token) {
        papelUsuarioAcessoPermitido(token);

        return alunoRepository.findAll().stream().map(aluno -> {
            String nomeTurma = aluno.getNomeTurma() != null ? aluno.getNomeTurma().getNome() : null;

            return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getData_nascimento(), aluno.getLogin().getLogin(), nomeTurma);
        }).toList();

    }
}

