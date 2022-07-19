package com.dockey.todoapi.controllers;

import com.dockey.todoapi.entities.Role;
import com.dockey.todoapi.entities.Todo;
import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.services.FileUploadUtil;
import com.dockey.todoapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.util.List;


@Controller
@Slf4j

public class HomeController {

    private final String UPLOAD_DIR = "C:\\uploads";

    @Autowired
    private UserService service;

    private final TodoRepository todoRepository;

    public HomeController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/")  // INDEX.HTML
    public String index(Model model) {
        log.info("Load index.html");
        return "index";
    }

    @GetMapping("/registration")  // USER REGISTRATION
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        log.info("Load user registration page");
        return "registration";
    }

    @PostMapping("/registration_process")  // SAVE USER REGISTRATION
    public String processRegister(User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setProfilkep(fileName);
        String uploadDir = "user-photos/" + user.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        service.registerDefaultUser(user);
        log.info("User registration / " + user);
        return "register_success";
    }

    @GetMapping("/users")  // LIST USERS
    public String listUsers(Model model, Authentication authentication) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("admin", authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        log.info("Logged user: " + authentication.getName() + " / List users");
        return "users";
    }

    @GetMapping("/users/edit/{id}")  // EDIT USER
    public String editUser(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User user = service.get(id);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / Edit user");
        return "user_form";
    }

    @DeleteMapping("/users/{id}")  // DELETE USER
    public void deleteUser(@PathVariable("id") Long id, Authentication authentication) {
        User user = service.get(id);
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / Delete user");
        service.removeUser(id);
    }

    @PostMapping("/users/save")  // SAVE USER
    public RedirectView saveUser(User user, Authentication authentication, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setProfilkep(fileName);
        User savedUser = service.save(user);
        String uploadDir = "user-photos/" + savedUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        log.info("Logged user: " + authentication.getName() + " User: " + savedUser.getUsername() + " / Save user / " + savedUser);
        return new RedirectView("/users", true);
    }

    @GetMapping("/users/{id}/todos")  // LIST TODOS
    public String listTodo(@PathVariable("id") Long id, Model model, Authentication authentication) {
        List<Todo> listTodos = service.listTodos(id);
        model.addAttribute("listTodos", listTodos);
        model.addAttribute("todoGid", id);
        User user = service.get(id);
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / List todos");
        return "todos";
    }

    @GetMapping("/users/todo/{todoId}")  // EDIT TODO
    public String editTodo(@PathVariable("todoId") Long todoId, Model model, Authentication authentication) {
        Todo todo = todoRepository.findById(todoId).get();
        model.addAttribute("todo", todo);
        User user = service.get(todo.getGid());
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / Edit todo");
        return "todo_form";
    }

    @PostMapping("/users/todo/save")  // SAVE TODO
    public String saveUser(Todo todo, Authentication authentication) {
        todoRepository.save(todo);
        User user = service.get(todo.getGid());
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / Save todo / " + todo);
        return "redirect:/users/" + todo.getGid() + "/todos";
    }

    @GetMapping("/users/{todoGid}/newtodo")  // NEW TODO
    public String newTodo(@PathVariable("todoGid") Long todoGid, Model model, Authentication authentication) {
        Todo todo = new Todo();
        todo.setGid(todoGid);
        model.addAttribute("todo", todo);
        User user = service.get(todo.getGid());
        log.info("Logged user: " + authentication.getName() + " User: " + user.getUsername() + " / New todo");
        return "todo_form";
    }

    //create = POST /users
    //update = Put /users/id
    //delete = delete /users/id
    //get 1  = get /users/id
    //get all  = get /users
}
