package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "docente")
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docente_id")
    private Long id;

    private String nome;

    private LocalDate data_entrada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity login;

}
