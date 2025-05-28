package com.tfg.pawhope.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Usuario usuario; // Puede ser null si no está autenticado

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @Column(nullable = false)
    private String estado; // "Pendiente", "Aprobada", "Rechazada"

    @Column(nullable = false)
    private String nombre;  // Nombre y apellidos del adoptante

    @Column(nullable = false)
    private String email;   // Correo electrónico

    @Column(nullable = false)
    private String telefono; // Teléfono de contacto

    @Column(nullable = false, length = 1000)
    private String motivo;   // Por qué desea adoptar


}

