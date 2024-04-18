package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "curso")
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long id;

    private String nome;

    /*
     @OneToMany(mappedBy = "curso") // nome do atributo curso na classe Turma
    private List<TurmaEntity> turmas;

    @OneToMany(mappedBy = "curso")
    private List<MateriaEntity> materias;
     */

}
