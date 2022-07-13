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

    @GetMapping("/")
    public String index(Model model) {
        log.info("index.html betoltese");
        return "index";
    }

    @GetMapping("/registration")  // USER REGISZTRACIO
    public String showRegistrationForm(Model model) {
        log.info("felhasznalo regisztracio oldal betoltese");
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration_process")  // USER REGISZTRACIO ELMENTESE
    public String processRegister(User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setProfilkep(fileName);
        String uploadDir = "user-photos/" + user.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        service.registerDefaultUser(user);
        log.info("felhasznalo regisztracio " + user.toString());
        return "register_success";
    }

    @GetMapping("/users")  // USEREK KILISTAZASA
    public String listUsers(Model model, Authentication authentication) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("admin", authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        log.info("Felhasznalo: " + authentication.getName() + " / Felhasznalok kilistazasa");
        return "users";
    }

    @GetMapping("/users/edit/{id}")  // USER SZERKESZTESE
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = service.get(id);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        log.info("Felhasznalo: " + user.getUsername() + " / Felhasznalo szerkesztese");
        return "user_form";
    }

    @DeleteMapping("/users/{id}")  // USER TORLESE
    public void deleteUser(@PathVariable("id") Long id){
        service.removeUser(id);
    }

    @PostMapping("/users/save")
    public RedirectView saveUser(User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setProfilkep(fileName);
        User savedUser = service.save(user);
        String uploadDir = "user-photos/" + savedUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new RedirectView("/users", true);
    }

    @GetMapping("/users/{id}/todos")  // TODO KILISTAZASA
    public String listTodo(@PathVariable("id") Long id, Model model) {
        List<Todo> listTodos = service.listTodos(id);
        model.addAttribute("listTodos", listTodos);
        model.addAttribute("todoGid", id);
        return "todos";
    }

    @PostMapping("/users/todo/save") // TODO SZERKESZTESENEK MENTESE
    public String saveUser(Todo todo) {
        var todoGid = todo.getGid();
        todoRepository.save(todo);
        return "redirect:/users/" + todoGid + "/todos";
    }

    @GetMapping("/users/todo/{todoId}")  // TODO SZERKESZTESE
    public String editTodo(@PathVariable("todoId") Long todoId, Model model) {
        Todo todo = todoRepository.findById(todoId).get();
        model.addAttribute("todo", todo);
        return "todo_form";
    }

    @GetMapping("/users/{todoGid}/newtodo")  // TODO LETREHOZASA
    public String newTodo(@PathVariable("todoGid") Long todoGid, Model model) {
        Todo todo = new Todo();
        todo.setGid(todoGid);
        model.addAttribute("todo", todo);
        return "todo_form";
    }

    //create = POST /users
    //update = Put /users/id
    //delete = delete /users/id
    //get 1  = get /users/id
    //get all  = get /users
}
