package com.tfg.pawhope.service;


import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.model.Usuario;

import java.util.Optional;

public interface UsuarioService {

    UsuarioDTO usuarioToDto (Usuario usuarioDTO);
    Usuario registrarUsuario(UsuarioDTO usuario);
    Usuario actualizar(UsuarioDTO user);
    Optional<Usuario> findByCorreo(String correo);
}
