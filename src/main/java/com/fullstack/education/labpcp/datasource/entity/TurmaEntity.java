package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "turma")
public class TurmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turma_id")
    private Long id;

    @Column(unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private DocenteEntity nomeProfessor;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private CursoEntity nomeCurso;

    @OneToMany(mappedBy = "nomeTurma", fetch = FetchType.EAGER)
    private List<AlunoEntity> alunos;

}
