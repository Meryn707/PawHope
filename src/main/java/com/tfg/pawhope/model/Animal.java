package com.tfg.pawhope.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "animales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private String raza;
    private int edad;

    @Column(length = 1000)
    private String descripcion;

    private String imagen; // URL o ruta al archivo de imagen

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Usuario responsable;
}
