package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "aluno")
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aluno_id")
    private Long id;

    @Column(unique = true)
    private String nome;

    private LocalDate data_nascimento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity login;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private TurmaEntity nomeTurma;


    @OneToMany(mappedBy = "nomeAluno", fetch = FetchType.EAGER)
    private List<NotaEntity> notas;


}
