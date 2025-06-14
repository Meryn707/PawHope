package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.UsuarioYaExisteException;
import com.tfg.pawhope.mapper.UsuarioMapper;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO usuarioToDto (Usuario usuario) {
        return usuarioMapper.toDto(usuario);
    }


    public Usuario registrarUsuario(UsuarioDTO usuarioDTO) {

        Optional <Usuario> usuarioOptional = usuarioRepository.findByCorreo(usuarioDTO.getCorreo());

        if (usuarioOptional.isPresent()) {

            throw new UsuarioYaExisteException("El correo ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO); // lo convertimos a entidad
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); //encriptamos la contraseña

        System.out.println("Usuario a guardar: " + usuario);

        return usuarioRepository.save(usuario);
    }


    public UsuarioDTO actualizar(UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioRepository.save(usuario);

        return usuarioToDto(usuario);
    }

    public Optional <UsuarioDTO> findByCorreo(String correo) {

        Optional <Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("El correo no existe");
        }

        UsuarioDTO dto = usuarioToDto(usuarioOptional.get());
        return Optional.of(dto);
    }

    public Optional <UsuarioDTO> findByIdUsuario(Long idUsuario) {

        Optional <Usuario> usuarioOptional = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("El usuario no existe");
        }
        return Optional.of(usuarioToDto(usuarioOptional.get()));
    }

    public Long obtenerIdUsuarioPorCorreo(String correo) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return usuario.getIdUsuario();
        } else {
            throw new RuntimeException("Usuario no encontrado con correo: " + correo);
        }
    }

}
