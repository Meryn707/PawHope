package com.tfg.pawhope.security;

import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository usuarioRepository; //pasa simular qeu se guarde

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Pedro");
        usuario.setCorreo("tests@correo.com");
        usuario.setContrasena(passwordEncoder.encode("1234"));

        usuarioRepository.save(usuario);
    }

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
                        .password("contrasena", "wrongpassword"))
                .andExpect(status().is3xxRedirection()) //3xx comprueba que la respuesta es una redireccion
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void login_exitoso_conCredencialesValidas() throws Exception {
        mockMvc.perform(formLogin("/perform_login")
                        .user("correo", "test@correo.com")
                        .password("contrasena", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}