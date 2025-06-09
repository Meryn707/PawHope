package com.tfg.pawhope.controller;

import com.tfg.pawhope.controller.web.UsuarioWebController;
import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.UsuarioYaExisteException;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import com.tfg.pawhope.service.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioWebControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioWebController usuarioWebController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioWebController).build();
    }

    @Test
    void registrarUsuario_Exitoso_redireccionaALogin() throws Exception {

        doNothing().when(usuarioServiceImpl).registrarUsuario(any(UsuarioDTO.class));

        mockMvc.perform(post("/web/usuarios")
                        .param("correo", "test@correo.com")
                        .param("contrasena", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void registrarUsuario_CorreoExistente_redireccionaARegistroConError() throws Exception {
        doThrow(new UsuarioYaExisteException("Correo ya registrado"))
                .when(usuarioServiceImpl).registrarUsuario(any(UsuarioDTO.class));

        mockMvc.perform(post("/web/usuarios")
                        .param("correo", "test@correo.com")
                        .param("contrasena", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registro"))
                .andExpect(flash().attributeExists("errorCorreoExistente"));
    }

    @Test
    void verNotificaciones_UsuarioExiste_muestraVistaNotificaciones() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(1L);

        when(authentication.getName()).thenReturn("test@correo.com");
        when(usuarioServiceImpl.findByCorreo("test@correo.com")).thenReturn(Optional.of(usuario));
        when(solicitudAdopcionServiceImpl.findByResponsableId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/web/usuarios/notificaciones").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("notificaciones"))
                .andExpect(model().attributeExists("solicitudes"));
    }

    @Test
    void verNotificaciones_UsuarioNoExiste_lanzaExcepcion() throws Exception {
        when(authentication.getName()).thenReturn("noexiste@correo.com");
        when(usuarioServiceImpl.findByCorreo("noexiste@correo.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/web/usuarios/notificaciones").principal(authentication))
                .andExpect(status().is5xxServerError());
    }
}
