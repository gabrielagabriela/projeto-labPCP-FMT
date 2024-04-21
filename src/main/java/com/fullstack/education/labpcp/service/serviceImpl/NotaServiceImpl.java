package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.NotaRequest;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import com.fullstack.education.labpcp.datasource.entity.AlunoEntity;
import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import com.fullstack.education.labpcp.datasource.entity.NotaEntity;
import com.fullstack.education.labpcp.datasource.repository.*;
import com.fullstack.education.labpcp.service.NotaService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotaServiceImpl implements NotaService {

    private final TokenService tokenService;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;
    private final TurmaRepository turmaRepository;

    public void papelUsuarioAcessoPermitido(String token){


        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(Objects.equals(nomePapel, "RECRUITER") || Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "ALUNO")){
            throw new RuntimeException("Apenas administradores e professores podem acessar os endpoints de nota!");
        }
    }
    @Override
    public NotaResponse criarNota(NotaRequest notaRequest, String token) {
        papelUsuarioAcessoPermitido(token);

        DocenteEntity docente = docenteRepository.findByNome(notaRequest.nomeProfessor()).orElseThrow(() -> new RuntimeException("Este professor não existe ou ainda não está cadastrado como um docente"));

        AlunoEntity aluno = alunoRepository.findByNome(notaRequest.nomeAluno())
                .orElseThrow(() -> new RuntimeException("Não há aluno cadastrado com este nome!"));

        boolean professorAssociadoTurma = turmaRepository.existsByNomeProfessorNomeAndAlunosNome(
                notaRequest.nomeProfessor(), notaRequest.nomeAluno());

        if (!professorAssociadoTurma) {
            throw new RuntimeException("Este docente não é professor do curso que o aluno está matriculado");
        }

        // Obtém o nome do curso do aluno a partir do nome do aluno
        String nomeCurso = aluno.getNomeTurma().getNomeCurso().getNome();

        // Verifica se a matéria corresponde ao curso do aluno
        boolean materiaDoCurso = turmaRepository.existsByNomeCursoNomeAndNomeCursoMateriasNome(
                nomeCurso, notaRequest.nomeMateria());
        if (!materiaDoCurso) {
            throw new RuntimeException("Esta matéria não corresponde ao curso do aluno");
        }

        NotaEntity nota = new NotaEntity();
        nota.setNomeAluno(aluno);
        nota.setNomeProfessor(docente);
        nota.setNomeMateria(materiaRepository.findByNome(notaRequest.nomeMateria())
                .orElseThrow(() -> new RuntimeException("Não há matéria cadastrada com este nome!")));
        nota.setValor(notaRequest.valor());
        nota.setData(notaRequest.data());

        notaRepository.save(nota);

        return new NotaResponse(nota.getId(), nota.getNomeAluno().getNome(), nota.getNomeProfessor().getNome(), nota.getNomeMateria().getNome(), nota.getValor(), nota.getData());
    }

     public NotaEntity notaPorId(Long id){
        NotaEntity notaPesquisada = notaRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhuma nota cadastrada"));
        return notaPesquisada;
    }

    @Override
    public NotaResponse obterNotaPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        NotaEntity notaPesquisada = notaPorId(id);
        return new NotaResponse(notaPesquisada.getId(), notaPesquisada.getNomeAluno().getNome(), notaPesquisada.getNomeProfessor().getNome(), notaPesquisada.getNomeMateria().getNome(), notaPesquisada.getValor(), notaPesquisada.getData());
    }

    @Override
    public NotaResponse atualizarNota(Long id, NotaRequest notaRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        NotaEntity notaPesquisada = notaPorId(id);

        DocenteEntity docente = docenteRepository.findByNome(notaRequest.nomeProfessor()).orElseThrow(() -> new RuntimeException("Este professor não existe ou ainda não está cadastrado como um docente"));

        AlunoEntity aluno = alunoRepository.findByNome(notaRequest.nomeAluno())
                .orElseThrow(() -> new RuntimeException("Não há aluno cadastrado com este nome!"));

        boolean professorAssociadoTurma = turmaRepository.existsByNomeProfessorNomeAndAlunosNome(
                notaRequest.nomeProfessor(), notaRequest.nomeAluno());

        if (!professorAssociadoTurma) {
            throw new RuntimeException("Este docente não é professor do curso que o aluno está matriculado");
        }

        // Obtém o nome do curso do aluno a partir do nome do aluno
        String nomeCurso = aluno.getNomeTurma().getNomeCurso().getNome();

        // Verifica se a matéria corresponde ao curso do aluno
        boolean materiaDoCurso = turmaRepository.existsByNomeCursoNomeAndNomeCursoMateriasNome(
                nomeCurso, notaRequest.nomeMateria());
        if (!materiaDoCurso) {
            throw new RuntimeException("Esta matéria não corresponde ao curso do aluno");
        }

        notaPesquisada.setId(id);
        notaPesquisada.setNomeAluno(aluno);
        notaPesquisada.setNomeProfessor(docente);
        notaPesquisada.setNomeMateria(materiaRepository.findByNome(notaRequest.nomeMateria()).orElseThrow(() -> new RuntimeException("Não há matéria cadastrada com este nome!")));
        notaPesquisada.setValor(notaRequest.valor());
        notaPesquisada.setData(notaRequest.data());

        notaRepository.save(notaPesquisada);

        return new NotaResponse(notaPesquisada.getId(), notaPesquisada.getNomeAluno().getNome(), notaPesquisada.getNomeProfessor().getNome(), notaPesquisada.getNomeMateria().getNome(), notaPesquisada.getValor(), notaPesquisada.getData());
    }

    @Override
    public void excluirNota(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem podem excluir nota!");
        }

        NotaEntity notaPesquisada = notaPorId(id);

        notaRepository.delete(notaPesquisada);
    }

    @Override
    public List<NotaResponse> listarTodasNotas(String token) {

        papelUsuarioAcessoPermitido(token);

        return notaRepository.findAll().stream().map(
                n -> new NotaResponse(n.getId(), n.getNomeAluno().getNome(), n.getNomeProfessor().getNome(), n.getNomeMateria().getNome(), n.getValor(), n.getData())
        ).toList();
    }
}
