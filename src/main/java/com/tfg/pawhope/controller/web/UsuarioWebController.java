package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.service.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller// para poder devolver vistas/redirecciones
@RequestMapping("/api/usuarios")
public class UsuarioWebController {

    private final UsuarioService usuarioService;
    public UsuarioWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registrarUsuario(UsuarioDTO usuarioDTO) {

        usuarioService.registrarUsuario(usuarioDTO);
        return "redirect:/login"; // Redirige a login tras registro exitoso
    }

}
