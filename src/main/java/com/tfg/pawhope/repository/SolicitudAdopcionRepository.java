package com.tfg.pawhope.repository;

import com.tfg.pawhope.model.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Integer> {

    List <SolicitudAdopcion> findByAnimalResponsableIdUsuario(Long idUsuario);
    Optional<SolicitudAdopcion> findById(Long id);
}
