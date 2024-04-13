package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PapelRepository extends JpaRepository<PapelEntity, Long> {
    Optional<PapelEntity> findByNome(String nome);
}
