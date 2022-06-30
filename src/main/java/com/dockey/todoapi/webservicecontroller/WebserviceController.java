package com.dockey.todoapi.webservicecontroller;

import com.dockey.todoapi.entities.Todo;
import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.Users;
import com.dockey.todoapi.entities.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WebserviceController {

    private final UsersRepository usersRepository;

    private final TodoRepository todoRepository;

    public WebserviceController(UsersRepository usersRepository, TodoRepository todoRepository){
        this.usersRepository = usersRepository;
        this.todoRepository = todoRepository;
        }

    @GetMapping("/users")
    public List<Users> getUser(){return usersRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    public Optional<Users> getTodogazda(@PathVariable("userId") Long userId) {
        var user = usersRepository.findById(userId);
        return user;
    }

    @PostMapping("/users")
    public Users newUser(@RequestBody Users users){return this.usersRepository.save(users);}

    @PutMapping("/users/{userId}")
    public Optional<Users> updateUser(@PathVariable("userId") Long userId, @RequestBody Users updatedUsers){
        return this.usersRepository.findById(userId).map(oldUsers -> this.usersRepository.save(updatedUsers));
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){this.usersRepository.deleteById(userId);}

    @GetMapping("/todo")
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    @GetMapping("/todo/{todoUsername}")
    public List<Todo> getTodo(@PathVariable("todoUsername") String todoUsername){
        return todoRepository.findByUsername(todoUsername);
    }

    @PostMapping
    public Todo newTodo(@RequestBody Todo todo){
        return this.todoRepository.save(todo);
    }

    @PutMapping("/todo/{todoId}")
    public Optional<Todo> updateTodo(@PathVariable("todoId") Long todoId, @RequestBody Todo updatedTodo){
        return this.todoRepository.findById(todoId).map(oldTodo -> this.todoRepository.save(updatedTodo));
    }

    @DeleteMapping("/todo/{todoId}")
    public void deleteTodo(@PathVariable("todoId") Long todoId){
        this.todoRepository.deleteById(todoId);
    }
    }
