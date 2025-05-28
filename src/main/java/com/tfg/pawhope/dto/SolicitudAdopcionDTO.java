package com.tfg.pawhope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
