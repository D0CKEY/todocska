package com.dockey.todoapi.controllers;

import com.dockey.todoapi.entities.Role;
import com.dockey.todoapi.entities.RoleRepository;
import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.entities.UserRepository;
import com.dockey.todoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller

public class HomeController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/registration")  // USER REGISZTRACIO
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration_process")  // USER REGISZTRACIO ELMENTESE
    public String processRegister(User user) {
        service.registerDefaultUser(user);
        return "register_success";
    }

    @GetMapping("/users")  // USEREK KILISTAZASA
    public String listUsers(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/users/edit/{id}")  // USER SZERKESZTESE
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = service.get(id);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }


    @PostMapping("/users/save") // USER SZERKESZTESENEK MENTESE
    public String saveUser(User user) {
        service.save(user);
        return "redirect:/users";
    }
}
