package com.dockey.todoapi.services;

import com.dockey.todoapi.entities.User;
import com.dockey.todoapi.entities.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthenticatedUserService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasId(Long id) {
        log.info("------------------------------------");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("------------------------------------" + username);
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        log.info("------------------------------------" + user.getId());
        return user.getId().equals(id);

    }

}