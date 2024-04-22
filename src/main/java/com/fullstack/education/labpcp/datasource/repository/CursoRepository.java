package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<CursoEntity, Long> {

    Optional<CursoEntity> findByNome(String nome);
}
