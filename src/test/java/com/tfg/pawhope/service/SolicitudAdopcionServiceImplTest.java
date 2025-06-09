package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.model.SolicitudAdopcion;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.AnimalRepository;
import com.tfg.pawhope.repository.SolicitudAdopcionRepository;
import com.tfg.pawhope.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudAdopcionServiceImplTest {

    @Mock
    private SolicitudAdopcionRepository solicitudRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitudAdopcionServiceImpl solicitudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearSolicitud_CreaCorrectamente() {

        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setIdAnimal(5L);
        dto.setNombre("Test");
        dto.setEmail("test@correo.com");
        dto.setTelefono("123456");
        dto.setMotivo("Porque sí");

        Animal animal = new Animal();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);


        when(animalRepository.findById(5L)).thenReturn(Optional.of(animal));
        when(usuarioRepository.findByCorreo("correo@prueba.com")).thenReturn(Optional.of(usuario));

        solicitudService.crearSolicitud(dto, "correo@prueba.com");

        verify(solicitudRepository).save(any(SolicitudAdopcion.class));
    }

    @Test
    void guardarSolicitud_ActualizaEstado() {
        SolicitudAdopcionDTO dto = new SolicitudAdopcionDTO();
        dto.setId(1L);
        dto.setEstado("ACEPTADA");

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setEstado("PENDIENTE");


        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));


        solicitudService.guardarSolicitud(dto);


        assertEquals("ACEPTADA", solicitud.getEstado());
        verify(solicitudRepository).save(solicitud);
    }
}
