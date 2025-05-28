package com.tfg.pawhope.mapper;

import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.model.SolicitudAdopcion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolicitudAdopcionMapper {

    SolicitudAdopcion toEntity(SolicitudAdopcionDTO dto);

    SolicitudAdopcionDTO toDto(SolicitudAdopcion entity);
}
