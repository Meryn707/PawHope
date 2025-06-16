package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.UsuarioYaExisteException;
import com.tfg.pawhope.mapper.UsuarioMapper;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioMapper usuarioMapper;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarUsuario_exitoso() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo("test@correo.com");
        usuarioDTO.setContrasena("1234");

        Usuario usuarioEntidad = new Usuario();
        usuarioEntidad.setCorreo("test@correo.com");
        usuarioEntidad.setContrasena("1234");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setCorreo("test@correo.com");
        usuarioGuardado.setContrasena("passwordEncriptado");


        when(usuarioRepository.findByCorreo("test@correo.com")).thenReturn(Optional.empty());
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuarioEntidad);
        when(passwordEncoder.encode("1234")).thenReturn("passwordEncriptado");
        when(usuarioRepository.save(usuarioEntidad)).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.registrarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("test@correo.com", resultado.getCorreo());
        assertEquals("passwordEncriptado", resultado.getContrasena());


        verify(usuarioRepository).findByCorreo("test@correo.com");
        verify(usuarioMapper).toEntity(usuarioDTO);
        verify(passwordEncoder).encode("1234");
        verify(usuarioRepository).save(usuarioEntidad);
    }

    @Test
    void registrarUsuario_correoExistente_lanzaExcepcion() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo("existe@correo.com");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setCorreo("existe@correo.com");

        when(usuarioRepository.findByCorreo("existe@correo.com")).thenReturn(Optional.of(usuarioExistente));

        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.registrarUsuario(usuarioDTO));

        verify(usuarioRepository).findByCorreo("existe@correo.com");
    }

    @Test
    void actualizar_usuario_existente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo("usuario@correo.com");
        usuarioDTO.setContrasena("password");

        Usuario usuarioEntidad = new Usuario();
        usuarioEntidad.setCorreo("usuario@correo.com");
        usuarioEntidad.setContrasena("password");

        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuarioEntidad);
        when(usuarioRepository.save(usuarioEntidad)).thenReturn(usuarioEntidad);
        when(usuarioMapper.toDto(usuarioEntidad)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.actualizar(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("usuario@correo.com", resultado.getCorreo());

        verify(usuarioMapper).toEntity(usuarioDTO);
        verify(usuarioRepository).save(usuarioEntidad);
        verify(usuarioMapper).toDto(usuarioEntidad);
    }

    @Test
    void findByCorreo_usuarioExistente() {
        String correo = "user@correo.com";
        Usuario usuarioEntidad = new Usuario();
        usuarioEntidad.setCorreo(correo);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo(correo);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuarioEntidad));
        when(usuarioMapper.toDto(usuarioEntidad)).thenReturn(usuarioDTO);

        Optional<UsuarioDTO> resultado = usuarioService.findByCorreo(correo);

        assertTrue(resultado.isPresent());
        assertEquals(correo, resultado.get().getCorreo());

        verify(usuarioRepository).findByCorreo(correo);
        verify(usuarioMapper).toDto(usuarioEntidad);
    }

    @Test
    void findByCorreo_usuarioNoExiste_lanzaExcepcion() {
        String correo = "noexiste@correo.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.findByCorreo(correo));

        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void obtenerIdUsuarioPorCorreo_usuarioExiste() {
        String correo = "id@correo.com";
        Usuario usuarioEntidad = new Usuario();
        usuarioEntidad.setCorreo(correo);
        usuarioEntidad.setIdUsuario(123L);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuarioEntidad));

        Long id = usuarioService.obtenerIdUsuarioPorCorreo(correo);

        assertEquals(123L, id);

        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void obtenerIdUsuarioPorCorreo_usuarioNoExiste_lanzaExcepcion() {

        String correo = "noid@correo.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerIdUsuarioPorCorreo(correo));

        verify(usuarioRepository).findByCorreo(correo);
    }
}