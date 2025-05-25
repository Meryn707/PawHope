package com.tfg.pawhope.mapper;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario user);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
