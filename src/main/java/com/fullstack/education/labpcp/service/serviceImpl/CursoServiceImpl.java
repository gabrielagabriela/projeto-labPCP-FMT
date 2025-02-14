package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.CursoRequest;
import com.fullstack.education.labpcp.controller.dto.response.CursoListaMateriaResponse;
import com.fullstack.education.labpcp.controller.dto.response.CursoResponse;
import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import com.fullstack.education.labpcp.datasource.entity.MateriaEntity;
import com.fullstack.education.labpcp.datasource.repository.CursoRepository;
import com.fullstack.education.labpcp.datasource.repository.MateriaRepository;
import com.fullstack.education.labpcp.datasource.repository.TurmaRepository;
import com.fullstack.education.labpcp.infra.exception.customException.*;
import com.fullstack.education.labpcp.service.CursoService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final TokenService tokenService;
    private final TurmaRepository turmaRepository;
    private final MateriaRepository materiaRepository;

    public void papelUsuarioAcessoPermitido(String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores e pedagogos podem acessar os endpoints de curso!");
        }
    }

    @Override
    public CursoResponse criarCurso(CursoRequest cursoRequest, String token) {

        if (cursoRequest.nome() == null) {
            throw new CampoAusenteException("O campo nome é obrigatório!");
        }


        Optional<CursoEntity> cursoExistenteOptional = cursoRepository.findByNome(cursoRequest.nome().toUpperCase());
        if (cursoExistenteOptional.isPresent()) {
            throw new RegistroExistenteException("Já existe curso cadastrado com este nome!");
        }


        papelUsuarioAcessoPermitido(token);
        CursoEntity curso = new CursoEntity();
        curso.setNome(cursoRequest.nome().toUpperCase());
        cursoRepository.save(curso);
        log.info("Curso criado -> com sucesso");
        return new CursoResponse(curso.getId(), curso.getNome());
    }

    public CursoEntity cursoPorId(Long id) {
        CursoEntity cursoPesquisado = cursoRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não correspondente a nenhum curso cadastrado"));
        return cursoPesquisado;
    }

    @Override
    public CursoResponse obterCursoPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        CursoEntity cursoPesquisado = cursoPorId(id);
        log.info("Curso obtido por ID -> com sucesso");
        return new CursoResponse(cursoPesquisado.getId(), cursoPesquisado.getNome());
    }

    @Override
    public CursoResponse atualizarCurso(Long id, CursoRequest cursoRequest, String token) {
        papelUsuarioAcessoPermitido(token);

        if (cursoRequest.nome() == null) {
            throw new CampoAusenteException("O campo nome é obrigatório!");

        }
        CursoEntity cursoPesquisado = cursoPorId(id);

        if (!cursoPesquisado.getNome().equalsIgnoreCase(cursoRequest.nome().toUpperCase())) {
            Optional<CursoEntity> cursoExistenteOptional = cursoRepository.findByNome(cursoRequest.nome().toUpperCase());
            if (cursoExistenteOptional.isPresent()) {
                throw new RegistroExistenteException("Já existe curso cadastrado com este nome!");
            }
        }

        cursoPesquisado.setId(id);
        cursoPesquisado.setNome(cursoRequest.nome().toUpperCase());
        cursoRepository.save(cursoPesquisado);
        log.info("Curso atualizado por ID -> com sucesso");
        return new CursoResponse(cursoPesquisado.getId(), cursoPesquisado.getNome());
    }

    @Override
    public void excluirCurso(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (!Objects.equals(nomePapel, "ADM")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores podem podem excluir curso!");
        }

        CursoEntity cursoPesquisado = cursoPorId(id);
        String nomeCursoPesquisado = cursoPesquisado.getNome();
        boolean cursoAssociadoAUmaTurma = turmaRepository.existsByNomeCursoNome(nomeCursoPesquisado);
        boolean cursoAssociadoAUmaMateria = materiaRepository.existsByNomeCursoNome(nomeCursoPesquisado);
        if (cursoAssociadoAUmaTurma ) {
            throw new BadRequestException("Este curso está associado a uma pelo menos uma turma, indique outro curso a turma antes de deleta-lo!");
        }
        if (cursoAssociadoAUmaMateria ) {
            throw new BadRequestException("Este curso está associado a uma matéria, indique outro curso a matéria antes de deleta-lo!");
        }

        log.info("Curso excluido por ID");
        cursoRepository.delete(cursoPesquisado);

    }

    @Override
    public List<CursoResponse> listarTodosCursos(String token) {
        papelUsuarioAcessoPermitido(token);
        log.info("Busca de Lista com todos os cursos cadastrados");
        return cursoRepository.findAll().stream().map(
                d -> new CursoResponse(d.getId(), d.getNome())
        ).toList();
    }

    @Override
    public List<CursoListaMateriaResponse> listarMateriasPorCurso(Long id, String token) {

        papelUsuarioAcessoPermitido(token);
        CursoEntity cursoPesquisado = cursoPorId(id);

        List<CursoListaMateriaResponse> resposta = new ArrayList<>();
        List<String> materias = new ArrayList<>();
        for (MateriaEntity materia : cursoPesquisado.getMaterias()) {
            materias.add(materia.getNome());
        }
        resposta.add(new CursoListaMateriaResponse(cursoPesquisado.getId(), cursoPesquisado.getNome(), materias));

        log.info("Busca de Lista de matérias de um curso por ID");
        return resposta;
    }
}

