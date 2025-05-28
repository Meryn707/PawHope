package com.tfg.pawhope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcionDTO {

    private Long id;
    private Long idUsuario;
    private Long idAnimal;
    private String estado;

    private String nombre;    // Nombre y apellidos del adoptante
    private String email;     // Correo electrónico
    private String telefono;  // Teléfono de contacto
    private String motivo;    // Por qué desea adoptar
}
