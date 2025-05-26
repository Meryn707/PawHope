package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.UsuarioYaExisteException;
import com.tfg.pawhope.service.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller// para poder devolver vistas/redirecciones
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    private final UsuarioService usuarioService;
    public UsuarioWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registrarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes ra) {
        try {
            usuarioService.registrarUsuario(usuarioDTO);
            return "redirect:/login"; // Redirige a login si éxito
        } catch (UsuarioYaExisteException e) {
            ra.addFlashAttribute("errorCorreoExistente", e.getMessage());
            return "redirect:/registro"; // Redirige a registro si error
        }
    }
}
