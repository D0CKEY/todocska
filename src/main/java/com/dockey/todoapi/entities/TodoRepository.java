package com.dockey.todoapi.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>{

    List<Todo> findAllByGid(Long gid);
}