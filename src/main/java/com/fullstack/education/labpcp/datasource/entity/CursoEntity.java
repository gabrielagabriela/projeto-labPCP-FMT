package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "curso")
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long id;

    @Column(unique = true)
    private String nome;

    @OneToMany(mappedBy = "nomeCurso", fetch = FetchType.EAGER)
    private List<TurmaEntity> turmas;

    @OneToMany(mappedBy = "nomeCurso", fetch = FetchType.EAGER)
    private List<MateriaEntity> materias;
}
