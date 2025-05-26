package com.tfg.pawhope.controller.api;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //Para probar la api en postman
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(value = "/registro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioCreado = usuarioService.registrarUsuario(usuarioDTO);
        UsuarioDTO dtoCreado = usuarioService.usuarioToDto(usuarioCreado);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreado);
    }

}
