package com.dockey.todoapi.web;

import com.dockey.todoapi.entities.TodoRepository;
import com.dockey.todoapi.entities.UsersRepository;
import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UsersRepository usersRepository;

    private final TodoRepository todoRepository;

    public UserController(UsersRepository usersRepository, TodoRepository todoRepository){
        this.usersRepository = usersRepository;
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public ModelAndView listUser(ModelAndView model) {
        model.addObject("users", usersRepository.findAll());
        model.addObject("todo", todoRepository.findAll());
        model.setViewName("users");
        return model;
    }

}
