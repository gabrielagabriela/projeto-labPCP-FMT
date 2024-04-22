package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {

    Optional<AlunoEntity> findByNome(String nome);
    boolean existsByLoginLogin(String login);
}
