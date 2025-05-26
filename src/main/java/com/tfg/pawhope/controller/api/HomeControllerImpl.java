package com.tfg.pawhope.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }
}
