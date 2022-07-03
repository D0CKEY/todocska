package com.dockey.todoapi.services;

import com.dockey.todoapi.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registerDefaultUser(User user) {
        encodePassword(user);
        userRepo.save(user);
        Role roleUser = roleRepo.findByName("ROLE_USER");
        user.addRole(roleUser);
        userRepo.save(user);
    }

    public void removeUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

    public User get(Long id) {return userRepo.findById(id).get(); }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        encodePassword(user);
        userRepo.save(user);
        }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public List<Todo> listTodos(Long gid) {
        return todoRepo.findAllByGid(gid);
    }
}