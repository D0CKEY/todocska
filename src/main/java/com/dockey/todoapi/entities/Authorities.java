package com.dockey.todoapi.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "authorities")
@Entity
@Getter
@Setter
@RequiredArgsConstructor

public class Authorities {
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "authority", nullable = false)
    private String authority;
    }

