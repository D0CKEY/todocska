package com.dockey.todoapi.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@RequiredArgsConstructor

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nev", nullable = false)
    private String nev;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profilkep", nullable = false)
    private String profilkep;

    @OneToMany
    @JoinColumn(name = "username", foreignKey = @ForeignKey( name = "FK_TODOGAZDA_ID" ) )
    private List<Todo> todos;
}
