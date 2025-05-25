package com.tfg.pawhope.service;


import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.model.Usuario;

public interface UsuarioService {

    UsuarioDTO usuarioToDto (Usuario usuarioDTO);
    Usuario registrarUsuario(UsuarioDTO usuario);
    Usuario actualizar(UsuarioDTO user);
}
