package com.tfg.pawhope.controller.impl;

import com.tfg.pawhope.dto.JwtResponseDTO;
import com.tfg.pawhope.dto.LoginDTO;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.security.JwtUtil;
import com.tfg.pawhope.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthControllerImpl(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO peticionLogin) {
        
        Usuario usuario = usuarioService.findByCorreo(peticionLogin.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(peticionLogin.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getCorreo());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
