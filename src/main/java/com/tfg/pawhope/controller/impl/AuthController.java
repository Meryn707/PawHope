package com.tfg.pawhope.controller.impl;

import com.tfg.pawhope.dto.LoginDTO;
import com.tfg.pawhope.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) {
        String token = authService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
        return ResponseEntity.ok().body(token);
    }
}
