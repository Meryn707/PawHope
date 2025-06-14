package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeControllerImpl {

    private final AnimalService animalService;

    public HomeControllerImpl(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Thymeleaf carga login.html
    }

    @GetMapping("/home")
    public String home() {
        return "inicio";  // Thymeleaf carga inicio.html
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @GetMapping("/about")
    public String sobreNosotros() {
        return "sobreNosotros";
    }

    @GetMapping("/") // Muestra la página principal directamente
    public String mostrarInicio(Model model) {
        List<AnimalDTO> animales = animalService.findAll();
        model.addAttribute("animales", animales);
        return "inicio";
    }
}
