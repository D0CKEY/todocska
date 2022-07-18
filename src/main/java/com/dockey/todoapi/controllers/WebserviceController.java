package com.dockey.todoapi.controllers;

import com.dockey.todoapi.entities.Todo;
import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.entities.UserRepository;
import com.dockey.todoapi.services.AuthenticatedUserService;
import com.dockey.todoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Optional;

@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
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

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")  // LIST USERS
    public List<User> getUser() { return userRepository.findAll(); }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // GET USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = "application/json")
    public Optional<User> getTodogazda(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId);
    }

    @RequestMapping(value = "/savenewuser", method = RequestMethod.POST, produces = "application/json")  // SAVE USER
    public User newUser(@RequestBody User users) { return this.userRepository.save(users); }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // UPDATE USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public Optional<User> updateUser(@PathVariable("userId") Long userId, @RequestBody User updatedUsers) {
        return this.userRepository.findById(userId).map(oldUsers -> this.userRepository.save(updatedUsers));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // DELETE USER
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteUser(@PathVariable("userId") Long userId){
        service.removeUser(userId);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // LIST USER'S TODOS
    @RequestMapping(value = "/users/{userId}/todos", method = RequestMethod.GET, produces = "application/json")
    public List<Todo> getTodos(@PathVariable("userId") Long userId) { return service.listTodos(userId); }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // GET USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos/{todoId}", method = RequestMethod.GET, produces = "application/json")
    public Optional<Todo> getTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId){
        return todoRepository.findById(todoId);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // SAVE USER'S TODO
    @RequestMapping(value = "/users/{userId}/todos", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    public Todo newTodo(@PathVariable("userId") Long userId, @RequestBody Todo todo){
        todo.setGid(userId);
        return this.todoRepository.save(todo);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or @authenticatedUserService.hasId(#userId)")  // UPDATE USER'S TODO
    @PutMapping("/users/{userId}/todos/{todoId}")
    public Optional<Todo> updateTodo(@PathVariable("todoId") Long todoId, @RequestBody Todo updatedTodo) {
        return this.todoRepository.findById(todoId).map(oldTodo -> this.todoRepository.save(updatedTodo));
    }

    @DeleteMapping("/users/todos/{todoId}")  // DELETE USER'S TODO
    public void deleteTodo(@PathVariable("todoId") Long todoId){
        this.todoRepository.deleteById(todoId);
    }
}
