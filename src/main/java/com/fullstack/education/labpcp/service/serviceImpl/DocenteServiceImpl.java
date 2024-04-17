package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.DocenteRequest;
import com.fullstack.education.labpcp.controller.dto.response.DocenteResponse;
import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import com.fullstack.education.labpcp.datasource.repository.DocenteRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.service.DocenteService;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public String papelUsuarioCadastrante(String token){

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(Objects.equals(nomePapel, "PROFESSOR") || Objects.equals(nomePapel, "ALUNO") ){
            throw new RuntimeException("Professor e aluno não podem acessar o endpoint de docentes!");
        }

        return nomePapel;
    }


    public void usuarioAcessaApenasProfessor(String nomePapel, boolean ehProfessor){

     if(Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "RECRUITER")) {
            if (!ehProfessor) {
                throw new RuntimeException("Pedagogo e Recruiter podem apenas acessar endpoint de docentes do tipo professor!");
            }
        }

     };

    @Override
    public DocenteResponse criarDocente(DocenteRequest docenteRequest, String token) {

        String nomePapel = papelUsuarioCadastrante(token);

        boolean usuarioAluno = usuarioRepository.existsByLoginAndPapelNome(docenteRequest.login(), "ALUNO");
        if(usuarioAluno){
            throw new RuntimeException("Este usuário tem papel de aluno");
        }

        boolean usuarioProfessor = usuarioRepository.existsByLoginAndPapelNome(docenteRequest.login(), "PROFESSOR");

        if(Objects.equals(nomePapel, "PEDAGOGICO") || Objects.equals(nomePapel, "RECRUITER")) {
            if (!usuarioProfessor) {
                throw new RuntimeException("Pedagogo e Recruiter podem apenas realizar cadastro de docentes do tipo professor!");
            }
        }

        DocenteEntity docente = new DocenteEntity();
        docente.setNome(docenteRequest.nome());
        docente.setData_entrada(docenteRequest.data_entrada());
        docente.setLogin(usuarioRepository.findByLogin(docenteRequest.login()).orElseThrow(() -> new RuntimeException("Login inválido")));
        docenteRepository.save(docente);

       return new DocenteResponse(docente.getId(), docente.getNome(), docente.getData_entrada(), docente.getLogin().getLogin());

    }

    public DocenteEntity docentePorId(Long id){
        DocenteEntity docentePesquisado = docenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhum docente cadastrado"));
        return docentePesquisado;
    }
    @Override
    public DocenteResponse obterDocentePorId(Long id, String token) {

        // verificar se tem acesso
        String nomePapel = papelUsuarioCadastrante(token);

        //DocenteEntity docentePesquisado = docenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhum docente cadastrado"));
        DocenteEntity docentePesquisado = docentePorId(id);

        // descobrir se o id é de prof para so deixar o nomePapel prof usar esse
        boolean usuarioProfessor = docenteRepository.existsByIdAndLoginPapelNome(id, "PROFESSOR");
        usuarioAcessaApenasProfessor(nomePapel, usuarioProfessor);

        return new DocenteResponse(docentePesquisado.getId(), docentePesquisado.getNome(), docentePesquisado.getData_entrada(), docentePesquisado.getLogin().getLogin());
    }

    @Override
    public DocenteResponse atualizarDocente(Long id, DocenteRequest docenteRequest, String token) {

        String nomePapel = papelUsuarioCadastrante(token);

        DocenteEntity docentePesquisado = docentePorId(id);

        boolean usuarioProfessor = docenteRepository.existsByIdAndLoginPapelNome(id, "PROFESSOR");
        usuarioAcessaApenasProfessor(nomePapel, usuarioProfessor);

        docentePesquisado.setId(id);
        docentePesquisado.setNome(docenteRequest.nome());
        docentePesquisado.setData_entrada(docenteRequest.data_entrada());
        docentePesquisado.setLogin(docentePesquisado.getLogin());

        docenteRepository.save(docentePesquisado);

        return new DocenteResponse(docentePesquisado.getId(), docentePesquisado.getNome(), docentePesquisado.getData_entrada(), docentePesquisado.getLogin().getLogin());
    }

    @Override
    public void excluirDocente(Long id, String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");
        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem excluir docente!");
        }

        DocenteEntity docentePesquisado = docentePorId(id);
        docenteRepository.delete(docentePesquisado);

    }

    @Override
    public List<DocenteResponse> listarTodosDocentes(String token) {

        String nomePapel = papelUsuarioCadastrante(token);

        List<DocenteEntity> docentes = docenteRepository.findAllByLoginPapelNome("PROFESSOR");

        if(!Objects.equals(nomePapel, "ADM")){

            return docentes.stream().map(
                    d -> new DocenteResponse(d.getId(), d.getNome(), d.getData_entrada(), d.getLogin().getLogin())
            ).toList();
        }

        return docenteRepository.findAll().stream().map(
                d -> new DocenteResponse(d.getId(), d.getNome(), d.getData_entrada(), d.getLogin().getLogin())
        ).toList();
    }
}


