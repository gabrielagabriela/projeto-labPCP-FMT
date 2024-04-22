package com.fullstack.education.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "nota")
public class NotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nota_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private AlunoEntity nomeAluno;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private DocenteEntity nomeProfessor;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    private MateriaEntity nomeMateria;

    private double valor;

    private LocalDate data;


}