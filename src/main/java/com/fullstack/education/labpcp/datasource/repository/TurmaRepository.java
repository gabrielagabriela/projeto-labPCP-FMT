package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import com.fullstack.education.labpcp.datasource.entity.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurmaRepository extends JpaRepository<TurmaEntity, Long> {
    Optional<TurmaEntity> findByNome(String nome);
}
