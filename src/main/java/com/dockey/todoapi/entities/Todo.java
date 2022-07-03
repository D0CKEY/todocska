package com.dockey.todoapi.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "gid", nullable = false)
    private Long gid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "megnevezes", nullable = false)
    private String megnevezes;

    @Column(name = "hatarido", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hatarido;

    @Column(name = "kesz", nullable = false)
    private Boolean kesz;

    @OneToMany
    @JoinColumn(name = "gid", foreignKey = @ForeignKey(name = "FK_USER_ID"))
    private List<Todo> todos;

}