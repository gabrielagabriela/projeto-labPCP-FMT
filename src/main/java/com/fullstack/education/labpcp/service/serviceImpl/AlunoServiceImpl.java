package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.AlunoRequest;
import com.fullstack.education.labpcp.controller.dto.response.AlunoResponse;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import com.fullstack.education.labpcp.controller.dto.response.PontuacaoTotalAlunoResponse;
import com.fullstack.education.labpcp.datasource.entity.AlunoEntity;
import com.fullstack.education.labpcp.datasource.entity.NotaEntity;
import com.fullstack.education.labpcp.datasource.repository.AlunoRepository;
import com.fullstack.education.labpcp.datasource.repository.TurmaRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.infra.exception.AcessoNaoAutorizadoException;
import com.fullstack.education.labpcp.infra.exception.NotFoundException;
import com.fullstack.education.labpcp.infra.exception.RegistroExistenteException;
import com.fullstack.education.labpcp.infra.exception.UsuarioIncompativelException;
import com.fullstack.education.labpcp.service.AlunoService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.text.DecimalFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService {


    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;


    public void papelUsuarioAcessoPermitido(String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores e pedagogos podem acessar os endpoints de aluno!");
        }
    }

    @Override
    public AlunoResponse criarAluno(AlunoRequest alunoRequest, String token) {

        if (alunoRequest.nome() == null || alunoRequest.data_nascimento() == null || alunoRequest.login() == null || alunoRequest.nomeTurma() == null) {
            throw new AcessoNaoAutorizadoException("Os campos nome, data_nascimento, login e nomeTurma são obrigatórios!");
        }

        papelUsuarioAcessoPermitido(token);


        Optional<AlunoEntity> alunoExistenteOptional = alunoRepository.findByNome(alunoRequest.nome().toUpperCase());
        if (alunoExistenteOptional.isPresent()) {
            throw new RegistroExistenteException("Já existe aluno cadastrado com este nome!");
        }


        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(alunoRequest.login().toLowerCase(), "ALUNO");
        if (!usuarioAluno) {
            throw new UsuarioIncompativelException("O login fornecido não pertence a um usuário de papel de aluno");
        }


        boolean loginJaUsado = alunoRepository.existsByLoginLogin(alunoRequest.login().toLowerCase());
        if (loginJaUsado) {
            throw new UsuarioIncompativelException("O login fornecido já foi cadastrado como aluno por um outro usuário");
        }


        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome(alunoRequest.nome().toUpperCase());
        aluno.setData_nascimento(alunoRequest.data_nascimento());
        aluno.setLogin(usuarioRepository.findByLogin(alunoRequest.login().toLowerCase()).orElseThrow(() -> new NotFoundException("Login inválido")));
        aluno.setNomeTurma(turmaRepository.findByNome(alunoRequest.nomeTurma().toUpperCase()).orElseThrow(() -> new NotFoundException("Não há turma cadastrada com esse nome!")));

        alunoRepository.save(aluno);

        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getData_nascimento(), aluno.getLogin().getLogin(), aluno.getNomeTurma().getNome());

    }


    public AlunoEntity alunoPorId(Long id) {
        AlunoEntity alunoPesquisado = alunoRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não correspondente a nenhum aluno cadastrado"));
        return alunoPesquisado;
    }

    @Override
    public AlunoResponse obterAlunoPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        AlunoEntity alunoPesquisado = alunoPorId(id);
        return new AlunoResponse(alunoPesquisado.getId(), alunoPesquisado.getNome(), alunoPesquisado.getData_nascimento(), alunoPesquisado.getLogin().getLogin(), alunoPesquisado.getNomeTurma().getNome());
    }

    @Override
    public AlunoResponse atualizarAluno(Long id, AlunoRequest alunoRequest, String token) {

        if (alunoRequest.nome() == null || alunoRequest.data_nascimento() == null || alunoRequest.login() == null || alunoRequest.nomeTurma() == null) {
            throw new AcessoNaoAutorizadoException("Os campos nome, data_nascimento, login e nomeTurma são obrigatórios!");
        }

        papelUsuarioAcessoPermitido(token);
        AlunoEntity alunoPesquisado = alunoPorId(id);


        if (!alunoPesquisado.getNome().equals(alunoRequest.nome().toUpperCase())) {
            Optional<AlunoEntity> alunoExistenteOptional = alunoRepository.findByNome(alunoRequest.nome().toUpperCase());
            if (alunoExistenteOptional.isPresent()) {
                throw new RegistroExistenteException("Já existe aluno cadastrado com este nome!");
            }
        }

        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(alunoRequest.login().toLowerCase(), "ALUNO");
        if (!usuarioAluno) {
            throw new UsuarioIncompativelException("O login fornecido não pertence a um usuário de papel de aluno");
        }


        if (!alunoPesquisado.getLogin().getLogin().equalsIgnoreCase(alunoRequest.login().toLowerCase())) {
            boolean loginJaUsado = alunoRepository.existsByLoginLogin(alunoRequest.login().toLowerCase());
            if (loginJaUsado) {
                throw new UsuarioIncompativelException("O login fornecido já foi cadastrado como aluno por um outro usuário");
            }
        }

        alunoPesquisado.setId(id);
        alunoPesquisado.setNome(alunoRequest.nome().toUpperCase());
        alunoPesquisado.setData_nascimento(alunoRequest.data_nascimento());
        alunoPesquisado.setLogin(usuarioRepository.findByLogin(alunoRequest.login().toLowerCase()).orElseThrow(() -> new NotFoundException("Login inválido")));
        alunoPesquisado.setNomeTurma(turmaRepository.findByNome(alunoRequest.nomeTurma().toUpperCase()).orElseThrow(() -> new NotFoundException("Não há turma cadastrada com esse nome!")));

        alunoRepository.save(alunoPesquisado);

        return new AlunoResponse(alunoPesquisado.getId(), alunoPesquisado.getNome(), alunoPesquisado.getData_nascimento(), alunoPesquisado.getLogin().getLogin(), alunoPesquisado.getNomeTurma().getNome());
    }

    @Override
    public void excluirAluno(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");
        if (!Objects.equals(nomePapel, "ADM")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores podem excluir aluno!");
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

    @Override
    public List<NotaResponse> listarNotasPorAluno(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores, pedagogos e o aluno podem acessar a lista de notas de um aluno!");
        }

        AlunoEntity alunoPesquisado = alunoPorId(id);


        if (nomePapel.equals("ALUNO")) {
            String subDoToken = tokenService.buscaCampo(token, "sub");
            Long idAlunoPesquisado = alunoPesquisado.getLogin().getId();

            if (!subDoToken.equals(String.valueOf(idAlunoPesquisado))) {
                throw new UsuarioIncompativelException("O ID fornecido não corresponde ao aluno buscando sua lista de notas!");
            }

        }
        List<NotaEntity> notas = alunoPesquisado.getNotas();
        return notas.stream().map(
                n -> new NotaResponse(n.getId(), n.getNomeAluno().getNome(), n.getNomeProfessor().getNome(), n.getNomeMateria().getNome(), n.getValor(), n.getData())
        ).toList();
    }

    @Override
    public PontuacaoTotalAlunoResponse pontuacaoTotalAluno(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores, pedagogos e o aluno podem acessar a pontuação do aluno!");
        }

        AlunoEntity alunoPesquisado = alunoPorId(id);

        if (nomePapel.equals("ALUNO")) {
            String subDoToken = tokenService.buscaCampo(token, "sub");
            Long idAlunoPesquisado = alunoPesquisado.getLogin().getId();

            if (!subDoToken.equals(String.valueOf(idAlunoPesquisado))) {
                throw new UsuarioIncompativelException("O ID fornecido não corresponde ao aluno buscando sua pontuação total!");
            }

        }

        double somaDasNotas = alunoPesquisado.getNotas().stream().mapToDouble(NotaEntity::getValor).sum();
        double quantidadeMateria = alunoPesquisado.getNomeTurma().getNomeCurso().getMaterias().size();
        double pontuacaoTotalDoAluno = somaDasNotas / quantidadeMateria * 10;

        return new PontuacaoTotalAlunoResponse(alunoPesquisado.getId(), alunoPesquisado.getNome(), pontuacaoTotalDoAluno);
    }

}

