package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {

   // boolean existsByIdAndUsuarioPapelNome(Long docenteId, String papelNome);

    boolean existsByIdAndLoginPapelNome(Long docenteId, String nomePapel);

    List<DocenteEntity> findAllByLoginPapelNome(String nomePapel);

}
