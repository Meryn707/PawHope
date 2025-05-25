package com.tfg.pawhope.controller.impl;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControllerImpl {

    private final UsuarioService usuarioService;
    public UsuarioControllerImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuarioCreado = usuarioService.registrarUsuario(usuarioDTO);
        UsuarioDTO dtoCreado = usuarioService.usuarioToDto(usuarioCreado); //devolvemos el user ya guardado e incriptado

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreado);
    }

}
