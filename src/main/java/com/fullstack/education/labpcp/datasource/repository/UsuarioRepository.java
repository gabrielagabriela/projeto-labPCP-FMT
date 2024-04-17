package com.fullstack.education.labpcp.datasource.repository;

import com.fullstack.education.labpcp.datasource.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByLogin(String login);
    boolean existsByLoginAndPapelNome(String login, String nomePapel);

}
