package com.fullstack.education.labpcp.datasource.entity;

import com.fullstack.education.labpcp.controller.dto.request.LoginRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(unique = true)
    private String login;

    private String senha;

    @ManyToOne
    @JoinColumn(name = "papel_id")
    private PapelEntity papel;

    public boolean senhaCorreta(
            LoginRequest loginRequest,
            BCryptPasswordEncoder bCryptEncoder
    ) {
        return bCryptEncoder.matches(
                loginRequest.senha(),
                this.senha
        );
    }

}
