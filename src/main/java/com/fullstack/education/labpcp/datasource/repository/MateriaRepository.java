package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import com.fullstack.education.labpcp.datasource.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository<MateriaEntity, Long> {
    Optional<MateriaEntity> findByNome(String nome);
    //boolean existsByNomeAndNomeCurso(String nomeMateria, String nomeCurso);
    //boolean existsByNomeAndNomeCurso(String nomeMateria, String nomeCurso);

    boolean existsByNomeAndNomeCurso(String nomeMateria, CursoEntity curso);



}

