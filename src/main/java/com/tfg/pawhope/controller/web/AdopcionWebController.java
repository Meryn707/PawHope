package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.service.AnimalServiceImpl;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/adopcion")
public class AdopcionWebController {

    private final SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl;
    private final AnimalServiceImpl animalServiceImpl;

    @Autowired
    public AdopcionWebController(AnimalServiceImpl animalServiceImpl, SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl) {
        this.solicitudAdopcionServiceImpl = solicitudAdopcionServiceImpl;
        this.animalServiceImpl = animalServiceImpl;
    }

    @GetMapping("/formulario/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        try {
            AnimalDTO animalDTO = animalServiceImpl.findByIdAnimal(id);
            model.addAttribute("animal", animalDTO);
            return "formulario-adopcion";
        } catch (AnimalNoExisteException ex) {
            redirectAttrs.addFlashAttribute("error", "Animal no encontrado");
            return "redirect:/web/animales";
        }
    }


    @PostMapping("/enviar")
    public String enviarSolicitud(
            @ModelAttribute SolicitudAdopcionDTO solicitudDTO,
            Authentication auth,
            RedirectAttributes redirectAttrs) {

        String correoUsuario = (auth != null && auth.isAuthenticated()) ? auth.getName() : null;

        try {
            solicitudAdopcionServiceImpl.crearSolicitud(solicitudDTO, correoUsuario);
            redirectAttrs.addFlashAttribute("exito", "¡Solicitud enviada correctamente!");
            return "redirect:/web/animales/" + solicitudDTO.getIdAnimal();
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

}
