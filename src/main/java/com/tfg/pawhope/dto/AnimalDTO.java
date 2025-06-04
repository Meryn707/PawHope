package com.tfg.pawhope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long idAnimal;

    @NotNull(message = "Nombre es obligatorio")
    @Size(min = 1, max = 100)
    private String nombre;

    @NotNull(message = "Especie es obligatoria")
    private String especie;

    @NotNull(message = "Raza es obligatoria")
    private String raza;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 50, message = "La edad máxima permitida es 50 años")
    private int anios;

    private String rangoEdad;
    private int meses;

    @NotNull
    @Size(min = 10, max = 1000)
    private String descripcion;

    private String imagenUrl;

    @NotNull(message = "El ID del responsable es obligatorio")
    private Long idUsuario; // ID del responsable

    private MultipartFile imagen;  // Para recibir el archivo en el form
}