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

    private String nombre;
    private String email;
    private String telefono;
    private String motivo;
}
