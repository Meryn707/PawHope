package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.mapper.UsuarioMapper;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder,UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO usuarioToDto (Usuario usuario) {
        return usuarioMapper.toDto(usuario);
    }

    public Usuario registrarUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByCorreo(usuarioDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("Correo ya registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO); // lo convertimos a entidad
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); //encriptamos la contraseña

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

}
