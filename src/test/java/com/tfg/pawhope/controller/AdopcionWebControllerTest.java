package com.tfg.pawhope.controller;

import com.tfg.pawhope.controller.web.AdopcionWebController;
import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.service.AnimalServiceImpl;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdopcionWebControllerTest {

    private AnimalServiceImpl animalService;
    private SolicitudAdopcionServiceImpl solicitudService;
    private AdopcionWebController controller;

    private Model model;
    private RedirectAttributes redirectAttrs;

    @BeforeEach
    void setUp() {
        animalService = mock(AnimalServiceImpl.class);
        solicitudService = mock(SolicitudAdopcionServiceImpl.class);
        controller = new AdopcionWebController(animalService, solicitudService);

        model = mock(Model.class);
        redirectAttrs = mock(RedirectAttributes.class);
    }

    @Test
    void mostrarFormulario_AnimalExiste_devuelveVista() {
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setIdAnimal(1L);

        when(animalService.findByIdAnimal(1L)).thenReturn(animalDTO);

        String vista = controller.mostrarFormulario(1L, model, redirectAttrs);

        assertEquals("formulario-adopcion", vista);
        verify(model).addAttribute(eq("animal"), any(AnimalDTO.class));
    }

    @Test
    void mostrarFormulario_AnimalNoExiste_redirige() {
        when(animalService.findByIdAnimal(1L)).thenThrow(new AnimalNoExisteException("no encontrado"));

        String result = controller.mostrarFormulario(1L, model, redirectAttrs);

        assertEquals("redirect:/web/animales", result);
        verify(redirectAttrs).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    void enviarSolicitud_valida_redirigeConExito() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("usuario@test.com");

        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setIdAnimal(5L);

        String result = controller.enviarSolicitud(dto, auth, redirectAttrs);

        assertEquals("redirect:/web/animales/5", result);
        verify(redirectAttrs).addFlashAttribute(eq("exito"), anyString());
        verify(solicitudService).crearSolicitud(dto, "usuario@test.com");
    }

    @Test
    void enviarSolicitud_conError_redirigeConError() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("usuario@test.com");

        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setIdAnimal(7L);

        doThrow(new RuntimeException("fallo")).when(solicitudService).crearSolicitud(dto, "usuario@test.com");

        String result = controller.enviarSolicitud(dto, auth, redirectAttrs);

        assertEquals("redirect:/", result);
        verify(redirectAttrs).addFlashAttribute(eq("error"), anyString());
    }
}
