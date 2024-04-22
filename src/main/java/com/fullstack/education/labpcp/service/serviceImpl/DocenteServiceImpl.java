package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.DocenteRequest;
import com.fullstack.education.labpcp.controller.dto.response.DocenteResponse;
import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import com.fullstack.education.labpcp.datasource.repository.DocenteRepository;
import com.fullstack.education.labpcp.datasource.repository.TurmaRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.infra.exception.customException.*;
import com.fullstack.education.labpcp.service.DocenteService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;

    public String papelUsuarioCadastrante(String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")) {
            throw new AcessoNaoAutorizadoException("Professor e aluno não podem acessar o endpoint de docentes!");
        }

        return nomePapel;
    }


    public void usuarioAcessaApenasProfessor(String nomePapel, boolean ehProfessor) {

        if (Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "RECRUITER")) {
            if (!ehProfessor) {
                throw new AcessoNaoAutorizadoException("Pedagogo e Recruiter podem apenas acessar endpoint de docentes do tipo professor!");
            }
        }

    }

    ;

    @Override
    public DocenteResponse criarDocente(DocenteRequest docenteRequest, String token) {

        if (docenteRequest.nome() == null || docenteRequest.data_entrada() == null || docenteRequest.login() == null) {
            throw new CampoAusenteException("Os campos nome, data_entrada e login são obrigatórios");
        }

        String nomePapel = papelUsuarioCadastrante(token);

        boolean loginJaUsado = docenteRepository.existsByLoginLogin(docenteRequest.login().toLowerCase());
        if (loginJaUsado) {
            throw new UsuarioIncompativelException("O login fornecido já foi cadastrado como docente por um outro usuário");
        }

        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(docenteRequest.login().toLowerCase(), "ALUNO");
        if (usuarioAluno) {
            throw new UsuarioIncompativelException("O login fornecido pertence a um usuário de papel de aluno");
        }

        boolean usuarioProfessor = usuarioRepository.existsByLoginAndPapelNome(docenteRequest.login().toLowerCase(), "PROFESSOR");

        if (Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "RECRUITER")) {
            if (!usuarioProfessor) {
                throw new AcessoNaoAutorizadoException("Pedagogo e Recruiter podem apenas realizar cadastro de docentes do tipo professor!");
            }
        }


        Optional<DocenteEntity> docenteExistenteOptional = docenteRepository.findByNome(docenteRequest.nome().toUpperCase());
        if (docenteExistenteOptional.isPresent()) {
            throw new RegistroExistenteException("Já existe docente cadastrado com este nome!");
        }

        DocenteEntity docente = new DocenteEntity();
        docente.setNome(docenteRequest.nome().toUpperCase());
        docente.setData_entrada(docenteRequest.data_entrada());
        docente.setLogin(usuarioRepository.findByLogin(docenteRequest.login().toLowerCase()).orElseThrow(() -> new NotFoundException("Login inexistente")));
        docenteRepository.save(docente);

        log.info("Criação de um docente com sucesso!");
        return new DocenteResponse(docente.getId(), docente.getNome(), docente.getData_entrada(), docente.getLogin().getLogin());

    }

    public DocenteEntity docentePorId(Long id) {
        DocenteEntity docentePesquisado = docenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não correspondente a nenhum docente cadastrado"));
        return docentePesquisado;
    }

    @Override
    public DocenteResponse obterDocentePorId(Long id, String token) {

        String nomePapel = papelUsuarioCadastrante(token);

        DocenteEntity docentePesquisado = docentePorId(id);

        boolean usuarioProfessor = docenteRepository.existsByIdAndLoginPapelNome(id, "PROFESSOR");
        usuarioAcessaApenasProfessor(nomePapel, usuarioProfessor);

        log.info("Busca por um docente pelo seu ID");
        return new DocenteResponse(docentePesquisado.getId(), docentePesquisado.getNome(), docentePesquisado.getData_entrada(), docentePesquisado.getLogin().getLogin());
    }

    @Override
    public DocenteResponse atualizarDocente(Long id, DocenteRequest docenteRequest, String token) {

        if (docenteRequest.nome() == null || docenteRequest.data_entrada() == null || docenteRequest.login() == null) {
            throw new CampoAusenteException("Os campos nome, data_entrada e login são obrigatórios");
        }

        String nomePapel = papelUsuarioCadastrante(token);

        DocenteEntity docentePesquisado = docentePorId(id);

        boolean usuarioProfessor = docenteRepository.existsByIdAndLoginPapelNome(id, "PROFESSOR");
        usuarioAcessaApenasProfessor(nomePapel, usuarioProfessor);

        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(docenteRequest.login().toLowerCase(), "ALUNO");
        if (usuarioAluno) {
            throw new UsuarioIncompativelException("O login fornecido pertence a um usuário com papel de aluno");
        }

        if (!docentePesquisado.getLogin().getLogin().equalsIgnoreCase(docenteRequest.login().toLowerCase())) {
            boolean loginJaUsado = docenteRepository.existsByLoginLogin(docenteRequest.login().toLowerCase());
            if (loginJaUsado) {
                throw new UsuarioIncompativelException("O login fornecido já foi cadastrado como docente por um outro usuário");
            }
        }

        if (!docentePesquisado.getNome().equals(docenteRequest.nome().toUpperCase())) {
            Optional<DocenteEntity> docenteExistenteOptional = docenteRepository.findByNome(docenteRequest.nome().toUpperCase());
            if (docenteExistenteOptional.isPresent()) {
                throw new RegistroExistenteException("Já existe docente cadastrado com este nome!");
            }
        }

        docentePesquisado.setId(id);
        docentePesquisado.setNome(docenteRequest.nome().toUpperCase());
        docentePesquisado.setData_entrada(docenteRequest.data_entrada());
        docentePesquisado.setLogin(usuarioRepository.findByLogin(docenteRequest.login().toLowerCase()).orElseThrow(() -> new NotFoundException("Login inexistente")));

        docenteRepository.save(docentePesquisado);

        log.info("Atualização dos dados de um docente pelo seu ID");
        return new DocenteResponse(docentePesquisado.getId(), docentePesquisado.getNome(), docentePesquisado.getData_entrada(), docentePesquisado.getLogin().getLogin());

    }

    @Override
    public void excluirDocente(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");
        if (!Objects.equals(nomePapel, "ADM")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores podem excluir docente!");
        }

        DocenteEntity docentePesquisado = docentePorId(id);
        String nomeDocentePesquisado = docentePesquisado.getNome();
        boolean professorEmUmaTurma = turmaRepository.existsByNomeProfessorNome(nomeDocentePesquisado);
        if (professorEmUmaTurma) {
            throw new BadRequestException("Este professor está associado a uma turma, antes de deleta-lo indique outro professor a turma");
        }
        log.info("Exclusão de um docente pelo seu ID");
        docenteRepository.delete(docentePesquisado);
    }

    @Override
    public List<DocenteResponse> listarTodosDocentes(String token) {

        String nomePapel = papelUsuarioCadastrante(token);

        List<DocenteEntity> docentes = docenteRepository.findAllByLoginPapelNome("PROFESSOR");

        if (!Objects.equals(nomePapel, "ADM")) {

            log.info("Busca da lista com todos os docentes de papel 'professor' cadastrados");
            return docentes.stream().map(
                    d -> new DocenteResponse(d.getId(), d.getNome(), d.getData_entrada(), (d.getLogin() != null) ? d.getLogin().getLogin() : null)
            ).toList();
        }

        log.info("Busca da lista com todos os docentes cadastrados");
        return docenteRepository.findAll().stream().map(
                d -> new DocenteResponse(d.getId(), d.getNome(), d.getData_entrada(), (d.getLogin() != null) ? d.getLogin().getLogin() : null)
        ).toList();
    }
}


