package com.tfg.pawhope.controller;

import com.tfg.pawhope.controller.web.AnimalWebController;
import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.service.AnimalServiceImpl;
import com.tfg.pawhope.service.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnimalWebControllerTest {

    private AnimalServiceImpl animalService;
    private UsuarioServiceImpl usuarioService;
    private AnimalWebController controller;

    private Model model;
    private RedirectAttributes redirectAttrs;
    private Authentication auth;

    @BeforeEach
    void setUp() {
        animalService = mock(AnimalServiceImpl.class);
        usuarioService = mock(UsuarioServiceImpl.class);
        controller = new AnimalWebController(animalService, usuarioService);

        model = mock(Model.class);
        redirectAttrs = mock(RedirectAttributes.class);
        auth = mock(Authentication.class);
    }

    @Test
    void detalleAnimal_existente_devuelveVista() {
        AnimalDTO animal = new AnimalDTO();
        when(animalService.findByIdAnimal(1L)).thenReturn(animal);

        String result = controller.detalleAnimal(1L, model, redirectAttrs);
        assertEquals("animal", result);
        verify(model).addAttribute(eq("animal"), eq(animal));
    }

    @Test
    void detalleAnimal_noExistente_redirige() {
        when(animalService.findByIdAnimal(1L)).thenThrow(new AnimalNoExisteException("No encontrado"));

        String result = controller.detalleAnimal(1L, model, redirectAttrs);
        assertEquals("redirect:/", result);
        verify(redirectAttrs).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    void listarAnimales_sinFiltros_devuelveTodos() {
        List<AnimalDTO> lista = new ArrayList<>();
        lista.add(new AnimalDTO());

        when(animalService.findAll()).thenReturn(lista);

        String result = controller.listarAnimales(null, null, model);
        assertEquals("inicio", result);
        verify(model).addAttribute("animales", lista);
    }

    @Test
    void verMisAnimales_usuarioAutenticado_muestraLista() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(99L);
        when(auth.getName()).thenReturn("user@test.com");
        when(usuarioService.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));

        List<Animal> animales = new ArrayList<>();
        when(animalService.findByResponsable_IdUsuario(99L)).thenReturn(animales);

        String result = controller.verMisAnimales(model, auth);
        assertEquals("mis_animales", result);
        verify(model).addAttribute("animales", animales);
    }

    @Test
    void eliminarAnimal_correcto_redirige() {
        AnimalDTO animal = new AnimalDTO();
        animal.setIdUsuario(88L);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(88L);

        when(animalService.findByIdAnimal(1L)).thenReturn(animal);
        when(auth.getName()).thenReturn("test@correo.com");
        when(usuarioService.findByCorreo("test@correo.com")).thenReturn(Optional.of(usuario));

        String result = controller.eliminarAnimal(1L, auth);

        assertEquals("redirect:/web/animales/mis-animales", result);
        verify(animalService).deleteAnimal(animal);
    }

    @Test
    void eliminarAnimal_usuarioNoCoincide_lanzaExcepcion() {
        AnimalDTO animal = new AnimalDTO();
        animal.setIdUsuario(1L);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(2L);

        when(animalService.findByIdAnimal(1L)).thenReturn(animal);
        when(auth.getName()).thenReturn("otro@correo.com");
        when(usuarioService.findByCorreo("otro@correo.com")).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () -> controller.eliminarAnimal(1L, auth));
    }
}
