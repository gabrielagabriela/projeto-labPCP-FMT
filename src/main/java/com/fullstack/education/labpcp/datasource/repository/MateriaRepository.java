package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository<MateriaEntity, Long> {
    Optional<MateriaEntity> findByNome(String nome);

    boolean existsByNomeCursoNome(String nomeCurso);
}

