package com.dockey.todoapi.controllers;

import com.dockey.todoapi.entities.Todo;
import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.entities.UserRepository;
import com.dockey.todoapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@RestController
@Slf4j
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

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")  // LIST USERS
    public List<User> getUser() {
        log.info("REST API - LIST USERS");
        return userRepository.findAll();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // GET USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = "application/json")
    public Optional<User> getTodogazda(@PathVariable("userId") Long userId) {
        log.info("REST API - GET USER");
        return userRepository.findById(userId);
    }

    @RequestMapping(value = "/savenewuser", method = RequestMethod.POST, produces = "application/json")  // SAVE USER
    public User newUser(@RequestBody User users) {
        log.info("REST API - SAVE USER");
        return this.userRepository.save(users);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // UPDATE USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public Optional<User> updateUser(@PathVariable("userId") Long userId, @RequestBody User updatedUsers) {
        log.info("REST API - UPDATE USER");
        return this.userRepository.findById(userId).map(oldUsers -> this.userRepository.save(updatedUsers));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // DELETE USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("REST API - DELETE USER");
        service.removeUser(userId);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // LIST USER'S TODOS
    @RequestMapping(value = "/users/{userId}/todos", method = RequestMethod.GET, produces = "application/json")
    public List<Todo> getTodos(@PathVariable("userId") Long userId) {
        log.info("REST API - LIST USER'S TODOS");
        return service.listTodos(userId);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // GET USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos/{todoId}", method = RequestMethod.GET, produces = "application/json")
    public Optional<Todo> getTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId){
        log.info("REST API - GET USER'S TODO");
        return todoRepository.findById(todoId);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // SAVE USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    public Todo newTodo(@PathVariable("userId") Long userId, @RequestBody Todo todo){
        todo.setGid(userId);
        log.info("REST API - SAVE USER'S TODO");
        return this.todoRepository.save(todo);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // UPDATE USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos/{todoId}", method = RequestMethod.PUT, produces = { "application/json; charset=utf-8" })
    public Optional<Todo> updateTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId, @RequestBody Todo updatedTodo) {
        log.info("REST API - UPDATE USER'S TODO");
        return this.todoRepository.findById(todoId).map(oldTodo -> this.todoRepository.save(updatedTodo));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // DELETE USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos/{todoId}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId) {
        log.info("REST API - DELETE USER'S TODO");
        this.todoRepository.deleteById(todoId);
    }
}
