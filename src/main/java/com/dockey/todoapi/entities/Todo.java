package com.dockey.todoapi.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "todo")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "megnevezes", nullable = false)
    private String megnevezes;

    @Column(name = "hatarido", nullable = false)
    private String hatarido;

    @Column(name = "kesz", nullable = false)
    private Boolean kesz;

}