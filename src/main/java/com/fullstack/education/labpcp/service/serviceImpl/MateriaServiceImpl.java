package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.MateriaRequest;
import com.fullstack.education.labpcp.controller.dto.response.MateriaResponse;
import com.fullstack.education.labpcp.datasource.entity.MateriaEntity;
import com.fullstack.education.labpcp.datasource.repository.CursoRepository;
import com.fullstack.education.labpcp.datasource.repository.MateriaRepository;
import com.fullstack.education.labpcp.service.TokenService;
import com.fullstack.education.labpcp.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final TokenService tokenService;
    private final CursoRepository cursoRepository;

    public void papelUsuarioAcessoPermitido(String token){

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administradores podem acessar os endpoints de materia!");
        }
    }

    @Override
    public MateriaResponse criarMateria(MateriaRequest materiaRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        MateriaEntity materia = new MateriaEntity();
        materia.setNome(materiaRequest.nome());
        materia.setNomeCurso(cursoRepository.findByNome(materiaRequest.nomeCurso()).orElseThrow(() -> new RuntimeException("Não há curso cadastrado com esse nome")));
        materiaRepository.save(materia);

        return new MateriaResponse(materia.getId(), materia.getNome(), materia.getNomeCurso().getNome());
    }

    public MateriaEntity materiaPorId(Long id){
        MateriaEntity materiaPesquisada = materiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Id não correspondente a nenhuma matéria cadastrada"));
        return materiaPesquisada;
    }
    @Override
    public MateriaResponse obterMateriaPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        MateriaEntity materiaPesquisada = materiaPorId(id);
        return new MateriaResponse(materiaPesquisada.getId(),materiaPesquisada.getNome(), materiaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public MateriaResponse atualizarMateria(Long id, MateriaRequest materiaRequest, String token) {

        papelUsuarioAcessoPermitido(token);

        MateriaEntity materiaPesquisada = materiaPorId(id);

        materiaPesquisada.setId(id);
        materiaPesquisada.setNome(materiaRequest.nome());
        materiaPesquisada.setNomeCurso(cursoRepository.findByNome(materiaRequest.nomeCurso()).orElseThrow(() -> new RuntimeException("Não há curso cadastrado com esse nome")));

        materiaRepository.save(materiaPesquisada);

        return new MateriaResponse(materiaPesquisada.getId(), materiaPesquisada.getNome(), materiaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public void excluirMateria(Long id, String token) {

        papelUsuarioAcessoPermitido(token);

        MateriaEntity materiaPesquisada = materiaPorId(id);
        materiaRepository.delete(materiaPesquisada);
    }
    @Override
    public List<MateriaResponse> listarTodasMaterias(String token) {

        papelUsuarioAcessoPermitido(token);

        return materiaRepository.findAll().stream().map(materia -> {
            String nomeCurso = materia.getNomeCurso() != null ? materia.getNomeCurso().getNome() : null;

            return new MateriaResponse(materia.getId(), materia.getNome(), nomeCurso);
        }).toList();
    }

}
