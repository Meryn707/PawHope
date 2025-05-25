package com.tfg.pawhope.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes_adopcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private Animal animal;

    private String estado; // "Pendiente", "Aprobada", "Rechazada", etc.
}

