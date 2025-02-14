package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "materia")
public class MateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "materia_id")
    private Long id;

    @Column(unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private CursoEntity nomeCurso;

    @OneToMany(mappedBy = "nomeMateria", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<NotaEntity> notas;

}
