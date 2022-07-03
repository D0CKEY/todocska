package com.dockey.todoapi.controllers;

import com.dockey.todoapi.entities.Todo;
import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.entities.UserRepository;
import com.dockey.todoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WebserviceController {

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    @Autowired
    private UserService service;

    public WebserviceController(UserRepository userRepository, TodoRepository todoRepository){
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    @GetMapping("/users")
    public List<User> getUser(){return userRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    public Optional<User> getTodogazda(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId);
    }

    @PostMapping("/users")
    public User newUser(@RequestBody User users){return this.userRepository.save(users);}

    @PutMapping("/users/{userId}")
    public Optional<User> updateUser(@PathVariable("userId") Long userId, @RequestBody User updatedUsers){
        return this.userRepository.findById(userId).map(oldUsers -> this.userRepository.save(updatedUsers));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        service.removeUser(id);
    }

    @GetMapping("/users/{userId}/todo")
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    @GetMapping("/users/{userId}/todo/{todoId}")
    public Optional<Todo> getTodo(@PathVariable("todoId") Long todoId){
        return todoRepository.findById(todoId);
    }

    @PostMapping("/users/{userId}/todo")
    public Todo newTodo(@RequestBody Todo todo){
        return this.todoRepository.save(todo);
    }

    @PutMapping("/users/{userId}/todo/{todoId}")
    public Optional<Todo> updateTodo(@PathVariable("todoId") Long todoId, @RequestBody Todo updatedTodo){
        return this.todoRepository.findById(todoId).map(oldTodo -> this.todoRepository.save(updatedTodo));
    }

    @DeleteMapping("/users/{userId}/todo/{todoId}")
    public void deleteTodo(@PathVariable("todoId") Long todoId){
        this.todoRepository.deleteById(todoId);
    }
}
