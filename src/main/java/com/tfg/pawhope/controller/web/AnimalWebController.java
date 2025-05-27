package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.service.AnimalServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/web/animales")
public class AnimalWebController {

    private final AnimalServiceImpl animalServiceImpl;

    public AnimalWebController(AnimalServiceImpl animalServiceImpl) {
        this.animalServiceImpl = animalServiceImpl;
    }



    @GetMapping("/{idAnimal}")
    public String detalleAnimal(
            @PathVariable("idAnimal") Long idAnimal,
            Model model,
            RedirectAttributes redirectAttrs) {

        try {
            AnimalDTO animal = animalServiceImpl.findByIdAnimal(idAnimal);
            model.addAttribute("animal", animal);
            return "animal";
        } catch (AnimalNoExisteException ex) {
            redirectAttrs.addFlashAttribute("error", "Animal no encontrado");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("animalDTO", new AnimalDTO());
        return "animales/formulario";
    }

    @PostMapping
    public String crearAnimal(@ModelAttribute AnimalDTO animalDTO, RedirectAttributes ra) {
        animalServiceImpl.guardarAnimal(animalDTO);
        ra.addFlashAttribute("exito", "Animal registrado correctamente");
        return "redirect:/web/animales";
    }
}
