package com.tfg.pawhope.security;

import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.repository.UsuarioRepository;
import com.tfg.pawhope.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest //simula que inicia la app real
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    /*@Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    @BeforeEach
    void setup() {

        usuario = new Usuario();
        usuario.setNombre("TEST");
        usuario.setCorreo("test@cosrreo.com");
        usuario.setContrasena(passwordEncoder.encode("123456"));

        usuarioRepository.save(usuario);
    }*/

    @Test
    void rutaProtegida_redirigeSinLogin() throws Exception {
        mockMvc.perform(get("/web/animales/registrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void login_fallido_contrasenaIncorrecta() throws Exception {
        mockMvc.perform(formLogin("/perform_login")
                        .user("correo", "test@correo.com")
                        .password("contrasena", "123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void login_exitoso_conCredencialesValidas() throws Exception {
        mockMvc.perform(formLogin("/perform_login")
                        .user("correo", "test@correo.com")
                        .password("contrasena", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
