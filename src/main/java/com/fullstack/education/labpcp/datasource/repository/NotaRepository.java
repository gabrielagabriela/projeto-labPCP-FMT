package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.CursoEntity;
import com.fullstack.education.labpcp.datasource.entity.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<NotaEntity, Long> {
   // List<NotaEntity> findAllByNomeAluno(String nomeAluno);
}
