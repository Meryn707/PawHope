package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.excepciones.UsuarioNoExisteException;
import com.tfg.pawhope.mapper.AnimalMapper;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.AnimalRepository;
import com.tfg.pawhope.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalServiceImplTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AnimalMapper animalMapper;

    @InjectMocks
    private AnimalServiceImpl animalService;

    private List<Animal> animales;
    private List<AnimalDTO> dtos;
    private AnimalDTO dto;
    private Animal animal;
    private Usuario usuario;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setIdUsuario(10L);

        animal = new Animal();
        animal.setIdAnimal(1L);

        animales = new ArrayList<>();
        animales.add(animal);


        dto = new AnimalDTO();
        dto.setIdUsuario(10L);
        dto.setIdAnimal(1L);
        dtos = Collections.singletonList(dto);
    }

    @Test
    void findAll_debeRetornarListaDTO() {

        when(animalRepository.findAll()).thenReturn(animales);
        when(animalMapper.listToDto(animales)).thenReturn(dtos);

        List<AnimalDTO> resultado = animalService.findAll();

        assertEquals(1, resultado.size());
        verify(animalRepository).findAll();
        verify(animalMapper).listToDto(animales);
    }

    @Test
    void findByIdAnimal_animalExiste_devuelveDTO() {

        Usuario responsable = new Usuario();
        responsable.setIdUsuario(10L);
        animal.setResponsable(responsable);

        when(animalRepository.findByIdAnimal(1L)).thenReturn(Optional.of(animal));
        when(animalMapper.toDto(animal)).thenReturn(dto);

        AnimalDTO resultado = animalService.findByIdAnimal(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAnimal());
        assertEquals(10L, resultado.getIdUsuario());
    }

    @Test
    void findByIdAnimal_animalNoExiste_lanzaExcepcion() {
        when(animalRepository.findByIdAnimal(1L)).thenReturn(Optional.empty());

        assertThrows(AnimalNoExisteException.class, () -> animalService.findByIdAnimal(1L));
    }


    @Test
    void guardarAnimal_usuarioExiste_devuelveDTO() {

        when(usuarioRepository.findByIdUsuario(10L)).thenReturn(Optional.of(usuario));
        when(animalMapper.toEntity(dto)).thenReturn(animal);
        when(animalRepository.save(animal)).thenReturn(animal);
        when(animalMapper.toDto(animal)).thenReturn(dto);

        AnimalDTO resultado = animalService.guardarAnimal(dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdUsuario());
        verify(animalRepository).save(any());//verifica que se llamó al método save
    }

    @Test
    void deleteAnimal_existe_loElimina() {

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        animalService.deleteAnimal(dto);

        verify(animalRepository).delete(animal);
    }


    @Test
    void calcularRangoEdad() {
        assertEquals("Cachorro", animalService.calcularRangoEdad(0));
        assertEquals("Joven", animalService.calcularRangoEdad(3));
        assertEquals("Adulto", animalService.calcularRangoEdad(7));
        assertEquals("Senior", animalService.calcularRangoEdad(11));
    }

}