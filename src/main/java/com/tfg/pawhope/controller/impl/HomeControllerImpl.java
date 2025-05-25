package com.tfg.pawhope.controller.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControllerImpl {

    @GetMapping("/login")
    public String login() {
        return "login"; // Thymeleaf carga login.html
    }

    @GetMapping("/home")
    public String home() {
        return "inicio";  // Thymeleaf carga home.html
    }
}
