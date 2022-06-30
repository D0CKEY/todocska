package com.dockey.todoapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j

public class LombokLoggingController {
    @RequestMapping("/lombok")
    public String index() {

        log.info("EZ EGY VALODI LOG????");

        return "Howdy! Check out the Logs to see the output...";
    }
}
