package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.NotaRequest;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import com.fullstack.education.labpcp.datasource.entity.AlunoEntity;
import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import com.fullstack.education.labpcp.datasource.entity.NotaEntity;
import com.fullstack.education.labpcp.datasource.repository.*;
import com.fullstack.education.labpcp.infra.exception.*;
import com.fullstack.education.labpcp.service.NotaService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaServiceImpl implements NotaService {

    private final TokenService tokenService;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;
    private final TurmaRepository turmaRepository;

    public void papelUsuarioAcessoPermitido(String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "ALUNO")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores e professores podem acessar os endpoints de nota!");
        }
    }

    @Override
    public NotaResponse criarNota(NotaRequest notaRequest, String token) {

        if (notaRequest.nomeAluno() == null || notaRequest.nomeProfessor() == null || notaRequest.nomeMateria() == null || notaRequest.data() == null) {
            throw new CampoAusenteException("Os campos nomeAluno, nomeProfessor, nomeMateria, valor e data são obrigatórios!");
        }

        papelUsuarioAcessoPermitido(token);

        DocenteEntity docente = docenteRepository.findByNome(notaRequest.nomeProfessor().toUpperCase()).orElseThrow(() -> new NotFoundException("Este professor não existe ou ainda não está cadastrado como um docente"));

        AlunoEntity aluno = alunoRepository.findByNome(notaRequest.nomeAluno().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Não há aluno cadastrado com este nome!"));

        boolean professorAssociadoTurma = turmaRepository.existsByNomeProfessorNomeAndAlunosNome(
                notaRequest.nomeProfessor().toUpperCase(), notaRequest.nomeAluno().toUpperCase());

        if (!professorAssociadoTurma) {
            throw new UsuarioIncompativelException("Este docente não é professor do curso que o aluno está matriculado");
        }

        String nomeCurso = aluno.getNomeTurma().getNomeCurso().getNome();

        boolean materiaDoCurso = turmaRepository.existsByNomeCursoNomeAndNomeCursoMateriasNome(
                nomeCurso, notaRequest.nomeMateria().toUpperCase());
        if (!materiaDoCurso) {
            throw new BadRequestException("Esta matéria não corresponde ao curso do aluno");
        }

        NotaEntity nota = new NotaEntity();
        nota.setNomeAluno(aluno);
        nota.setNomeProfessor(docente);
        nota.setNomeMateria(materiaRepository.findByNome(notaRequest.nomeMateria().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Não há matéria cadastrada com este nome!")));
        nota.setValor(notaRequest.valor());
        nota.setData(notaRequest.data());

        notaRepository.save(nota);

        log.info("Cadastro de uma nota de um aluno -> sucesso!");
        return new NotaResponse(nota.getId(), nota.getNomeAluno().getNome(), nota.getNomeProfessor().getNome(), nota.getNomeMateria().getNome(), nota.getValor(), nota.getData());
    }

    public NotaEntity notaPorId(Long id) {
        NotaEntity notaPesquisada = notaRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não correspondente a nenhuma nota cadastrada"));
        return notaPesquisada;
    }

    @Override
    public NotaResponse obterNotaPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        NotaEntity notaPesquisada = notaPorId(id);
        log.info("Busca de uma nota pelo seu ID");
        return new NotaResponse(notaPesquisada.getId(), notaPesquisada.getNomeAluno().getNome(), notaPesquisada.getNomeProfessor().getNome(), notaPesquisada.getNomeMateria().getNome(), notaPesquisada.getValor(), notaPesquisada.getData());
    }

    @Override
    public NotaResponse atualizarNota(Long id, NotaRequest notaRequest, String token) {

        if (notaRequest.nomeAluno() == null || notaRequest.nomeProfessor() == null || notaRequest.nomeMateria() == null || notaRequest.data() == null) {
            throw new CampoAusenteException("Os campos nomeAluno, nomeProfessor, nomeMateria, valor e data são obrigatórios!");
        }

        papelUsuarioAcessoPermitido(token);

        NotaEntity notaPesquisada = notaPorId(id);

        DocenteEntity docente = docenteRepository.findByNome(notaRequest.nomeProfessor().toUpperCase()).orElseThrow(() -> new NotFoundException("Este professor não existe ou ainda não está cadastrado como um docente"));

        AlunoEntity aluno = alunoRepository.findByNome(notaRequest.nomeAluno().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Não há aluno cadastrado com este nome!"));

        boolean professorAssociadoTurma = turmaRepository.existsByNomeProfessorNomeAndAlunosNome(
                notaRequest.nomeProfessor().toUpperCase(), notaRequest.nomeAluno().toUpperCase());

        if (!professorAssociadoTurma) {
            throw new BadRequestException("Este docente não é professor do curso que o aluno está matriculado");
        }

        String nomeCurso = aluno.getNomeTurma().getNomeCurso().getNome();

        boolean materiaDoCurso = turmaRepository.existsByNomeCursoNomeAndNomeCursoMateriasNome(
                nomeCurso, notaRequest.nomeMateria().toUpperCase());
        if (!materiaDoCurso) {
            throw new BadRequestException("Esta matéria não corresponde ao curso do aluno");
        }

        notaPesquisada.setId(id);
        notaPesquisada.setNomeAluno(aluno);
        notaPesquisada.setNomeProfessor(docente);
        notaPesquisada.setNomeMateria(materiaRepository.findByNome(notaRequest.nomeMateria().toUpperCase()).orElseThrow(() -> new NotFoundException("Não há matéria cadastrada com este nome!")));
        notaPesquisada.setValor(notaRequest.valor());
        notaPesquisada.setData(notaRequest.data());

        notaRepository.save(notaPesquisada);

        log.info("Atualização dos campos de uma nota pelo seu ID");
        return new NotaResponse(notaPesquisada.getId(), notaPesquisada.getNomeAluno().getNome(), notaPesquisada.getNomeProfessor().getNome(), notaPesquisada.getNomeMateria().getNome(), notaPesquisada.getValor(), notaPesquisada.getData());
    }

    @Override
    public void excluirNota(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (!Objects.equals(nomePapel, "ADM")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores podem podem excluir nota!");
        }

        NotaEntity notaPesquisada = notaPorId(id);

        log.info("Exclusão de uma nota pelo seu ID");
        notaRepository.delete(notaPesquisada);
    }

    @Override
    public List<NotaResponse> listarTodasNotas(String token) {

        papelUsuarioAcessoPermitido(token);

        log.info("Lista com todas as notas cadastradas no sistema");
        return notaRepository.findAll().stream().map(
                n -> new NotaResponse(n.getId(), n.getNomeAluno().getNome(), n.getNomeProfessor().getNome(), n.getNomeMateria().getNome(), n.getValor(), n.getData())
        ).toList();
    }
}
