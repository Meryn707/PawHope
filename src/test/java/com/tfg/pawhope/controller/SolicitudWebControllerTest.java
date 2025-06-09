package com.tfg.pawhope.controller;

import com.tfg.pawhope.controller.web.SolicitudWebController;
import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SolicitudWebControllerTest {

    @Mock
    private SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl;

    @InjectMocks
    private SolicitudWebController solicitudWebController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(solicitudWebController).build();
    }

    @Test
    void verDetalleNotificacion_solicitudExiste_devuelveVista() throws Exception {
        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setId(1L);
        when(solicitudAdopcionServiceImpl.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/web/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("notificacion-detalle"))
                .andExpect(model().attributeExists("solicitud"));
    }

    @Test
    void verDetalleNotificacion_solicitudNoExiste_lanzaExcepcion() throws Exception {
        when(solicitudAdopcionServiceImpl.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/web/solicitudes/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void actualizarEstado_actualizaYRedirige() throws Exception {
        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setId(1L);
        when(solicitudAdopcionServiceImpl.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(post("/web/solicitudes/estado/1")
                        .param("nuevoEstado", "Aceptado"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/usuarios/notificaciones"));
    }
}
