package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.MateriaRequest;
import com.fullstack.education.labpcp.controller.dto.response.MateriaResponse;
import com.fullstack.education.labpcp.datasource.entity.MateriaEntity;
import com.fullstack.education.labpcp.datasource.repository.CursoRepository;
import com.fullstack.education.labpcp.datasource.repository.MateriaRepository;
import com.fullstack.education.labpcp.infra.exception.customException.AcessoNaoAutorizadoException;
import com.fullstack.education.labpcp.infra.exception.customException.CampoAusenteException;
import com.fullstack.education.labpcp.infra.exception.customException.NotFoundException;
import com.fullstack.education.labpcp.infra.exception.customException.RegistroExistenteException;
import com.fullstack.education.labpcp.service.TokenService;
import com.fullstack.education.labpcp.service.MateriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final TokenService tokenService;
    private final CursoRepository cursoRepository;

    public void papelUsuarioAcessoPermitido(String token) {

        String nomePapel = tokenService.buscaCampo(token, "scope");

        if (!Objects.equals(nomePapel, "ADM")) {
            throw new AcessoNaoAutorizadoException("Apenas administradores podem acessar os endpoints de materia!");
        }
    }

    @Override
    public MateriaResponse criarMateria(MateriaRequest materiaRequest, String token) {

        if (materiaRequest.nome() == null || materiaRequest.nomeCurso() == null) {
            throw new CampoAusenteException("O campo nome e nomeCurso são obrigatorios!");
        }

        papelUsuarioAcessoPermitido(token);


        Optional<MateriaEntity> materiaExistenteOptional = materiaRepository.findByNome(materiaRequest.nome().toUpperCase());
        if (materiaExistenteOptional.isPresent()) {
            throw new RegistroExistenteException("Já existe matéria cadastrada com este nome!");
        }


        MateriaEntity materia = new MateriaEntity();
        materia.setNome(materiaRequest.nome().toUpperCase());
        materia.setNomeCurso(cursoRepository.findByNome(materiaRequest.nomeCurso().toUpperCase()).orElseThrow(() -> new NotFoundException("Não há curso cadastrado com esse nome")));
        materiaRepository.save(materia);

        log.info("Criação de uma matéria com sucesso");
        return new MateriaResponse(materia.getId(), materia.getNome(), materia.getNomeCurso().getNome());
    }

    public MateriaEntity materiaPorId(Long id) {
        MateriaEntity materiaPesquisada = materiaRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não correspondente a nenhuma matéria cadastrada"));
        return materiaPesquisada;
    }

    @Override
    public MateriaResponse obterMateriaPorId(Long id, String token) {
        papelUsuarioAcessoPermitido(token);
        MateriaEntity materiaPesquisada = materiaPorId(id);
        log.info("Busca de uma matéria pelo seu ID");
        return new MateriaResponse(materiaPesquisada.getId(), materiaPesquisada.getNome(), materiaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public MateriaResponse atualizarMateria(Long id, MateriaRequest materiaRequest, String token) {

        if (materiaRequest.nome() == null || materiaRequest.nomeCurso() == null) {
            throw new CampoAusenteException("O campo nome e nomeCurso são obrigatorios!");
        }

        papelUsuarioAcessoPermitido(token);

        MateriaEntity materiaPesquisada = materiaPorId(id);


        if (!materiaPesquisada.getNome().equals(materiaRequest.nome().toUpperCase())) {
            Optional<MateriaEntity> materiaExistenteOptional = materiaRepository.findByNome(materiaRequest.nome().toUpperCase());
            if (materiaExistenteOptional.isPresent()) {
                throw new RegistroExistenteException("Já existe matéria cadastrada com este nome!");
            }
        }


        materiaPesquisada.setId(id);
        materiaPesquisada.setNome(materiaRequest.nome());
        materiaPesquisada.setNomeCurso(cursoRepository.findByNome(materiaRequest.nomeCurso().toUpperCase()).orElseThrow(() -> new NotFoundException("Não há curso cadastrado com esse nome")));

        materiaRepository.save(materiaPesquisada);

        log.info("Atualização dos campos de uma matéria pelo seu ID");
        return new MateriaResponse(materiaPesquisada.getId(), materiaPesquisada.getNome(), materiaPesquisada.getNomeCurso().getNome());
    }

    @Override
    public void excluirMateria(Long id, String token) {

        papelUsuarioAcessoPermitido(token);

        MateriaEntity materiaPesquisada = materiaPorId(id);
        log.info("Exclusão de uma matéria pelo seu ID - com sucesso!");
        materiaRepository.delete(materiaPesquisada);
    }

    @Override
    public List<MateriaResponse> listarTodasMaterias(String token) {

        papelUsuarioAcessoPermitido(token);

        log.info("Busca da lista com todas as matérias cadastradas no sistema");
        return materiaRepository.findAll().stream().map(materia -> {
            String nomeCurso = materia.getNomeCurso() != null ? materia.getNomeCurso().getNome() : null;

            return new MateriaResponse(materia.getId(), materia.getNome(), nomeCurso);
        }).toList();
    }

}
