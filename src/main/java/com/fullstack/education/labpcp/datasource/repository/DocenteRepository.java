package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {

   // boolean existsByIdAndUsuarioPapelNome(Long docenteId, String papelNome);

    boolean existsByIdAndLoginPapelNome(Long docenteId, String nomePapel);

    List<DocenteEntity> findAllByLoginPapelNome(String nomePapel);

    boolean existsByNomeAndLoginPapelNome(String nome, String nomePapel);

    Optional<DocenteEntity> findByNome(String nome);

    boolean existsByLoginLogin(String login);

}
