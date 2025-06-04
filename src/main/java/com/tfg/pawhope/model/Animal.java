package com.tfg.pawhope.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "animales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnimal;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Column(nullable = false)
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    @Column(nullable = false)
    private String raza;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 50, message = "La edad no puede ser mayor a 50")
    @Column(nullable = false)
    private int anios;
    @Column(nullable = false)
    private int meses;

    private String rangoEdad;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @NotBlank(message = "La URL de imagen es obligatoria")
    @Column(nullable = false)
    private String imagenUrl;

    @NotNull(message = "El responsable es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario responsable;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.REMOVE, orphanRemoval = true) //si se borra este animal tmabién las solicitudes asociadas
    private List<SolicitudAdopcion> solicitudes;

    @Lob
    private byte[] imagen;
}