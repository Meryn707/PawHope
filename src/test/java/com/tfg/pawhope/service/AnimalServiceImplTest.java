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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalServiceImplTest {

    private AnimalRepository animalRepository;
    private UsuarioRepository usuarioRepository;
    private AnimalMapper animalMapper;
    private AnimalServiceImpl animalService;

    @BeforeEach
    void setUp() {
        animalRepository = mock(AnimalRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        animalMapper = mock(AnimalMapper.class);
        animalService = new AnimalServiceImpl(animalRepository, usuarioRepository, animalMapper);
    }

    @Test
    void findAll_debeRetornarListaDTO() {
        Animal animal = new Animal();
        List<Animal> animales = Collections.singletonList(animal);
        AnimalDTO dto = new AnimalDTO();
        List<AnimalDTO> dtos = Collections.singletonList(dto);

        when(animalRepository.findAll()).thenReturn(animales);
        when(animalMapper.listToDto(animales)).thenReturn(dtos);

        List<AnimalDTO> resultado = animalService.findAll();

        assertEquals(1, resultado.size());
        verify(animalRepository).findAll();
        verify(animalMapper).listToDto(animales);
    }

    @Test
    void findByIdAnimal_animalExiste_devuelveDTO() {
        Animal animal = new Animal();
        animal.setIdAnimal(1L);
        Usuario responsable = new Usuario();
        responsable.setIdUsuario(2L);
        animal.setResponsable(responsable);

        AnimalDTO dto = new AnimalDTO();

        when(animalRepository.findByIdAnimal(1L)).thenReturn(Optional.of(animal));
        when(animalMapper.toDto(animal)).thenReturn(dto);

        AnimalDTO resultado = animalService.findByIdAnimal(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAnimal());
        assertEquals(2L, resultado.getIdUsuario());
    }

    @Test
    void findByIdAnimal_animalNoExiste_lanzaExcepcion() {
        when(animalRepository.findByIdAnimal(1L)).thenReturn(Optional.empty());

        assertThrows(AnimalNoExisteException.class, () -> animalService.findByIdAnimal(1L));
    }

    @Test
    void guardarAnimal_usuarioNoExiste_lanzaExcepcion() {
        AnimalDTO dto = new AnimalDTO();
        dto.setIdUsuario(10L);

        when(usuarioRepository.findByIdUsuario(10L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoExisteException.class, () -> animalService.guardarAnimal(dto));
    }

    @Test
    void guardarAnimal_usuarioExiste_guardaAnimal() {
        AnimalDTO dto = new AnimalDTO();
        dto.setIdUsuario(10L);
        dto.setImagenUrl("url");
        dto.setAnios(3);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);

        Animal animalEntity = new Animal();
        Animal animalGuardado = new Animal();
        animalGuardado.setIdAnimal(5L);
        animalGuardado.setResponsable(usuario);

        AnimalDTO dtoGuardado = new AnimalDTO();

        when(usuarioRepository.findByIdUsuario(10L)).thenReturn(Optional.of(usuario));
        when(animalMapper.toEntity(dto)).thenReturn(animalEntity);
        when(animalRepository.save(animalEntity)).thenReturn(animalGuardado);
        when(animalMapper.toDto(animalGuardado)).thenReturn(dtoGuardado);

        AnimalDTO resultado = animalService.guardarAnimal(dto);

        assertNotNull(resultado);
        verify(animalRepository).save(animalEntity);
    }

    @Test
    void calcularRangoEdad() {
        assertEquals("Cachorro", animalService.calcularRangoEdad(0));
        assertEquals("Joven", animalService.calcularRangoEdad(3));
        assertEquals("Adulto", animalService.calcularRangoEdad(7));
        assertEquals("Senior", animalService.calcularRangoEdad(11));
    }

    @Test
    void filtrarPorEspecie_conEspecie_llamaFindByEspecie() {
        String especie = "perro";
        List<Animal> lista = Collections.emptyList();
        when(animalRepository.findByEspecie(especie)).thenReturn(lista);

        List<Animal> resultado = animalService.filtrarPorEspecie(especie);

        assertSame(lista, resultado);
        verify(animalRepository).findByEspecie(especie);
    }

    @Test
    void filtrarPorEspecie_sinEspecie_llamaFindAll() {
        List<Animal> lista = Collections.emptyList();
        when(animalRepository.findAll()).thenReturn(lista);

        List<Animal> resultado = animalService.filtrarPorEspecie(null);

        assertSame(lista, resultado);
        verify(animalRepository).findAll();
    }

    @Test
    void deleteAnimal_animalNoExiste_lanzaExcepcion() {
        AnimalDTO dto = new AnimalDTO();
        dto.setIdAnimal(1L);

        when(animalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> animalService.deleteAnimal(dto));
    }

    @Test
    void deleteAnimal_animalExiste_eliminaAnimal() {
        AnimalDTO dto = new AnimalDTO();
        dto.setIdAnimal(1L);

        Animal animal = new Animal();
        animal.setIdAnimal(1L);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        animalService.deleteAnimal(dto);

        verify(animalRepository).delete(animal);
    }
}
