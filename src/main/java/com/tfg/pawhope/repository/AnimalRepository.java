package com.tfg.pawhope.repository;

import com.tfg.pawhope.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByIdAnimal(Long idAnimal);

    List<Animal> findByEspecie(String especie);

    List<Animal> findByAniosAndMeses(Integer anios, Integer meses);

    List<Animal> findByAnios(Integer anios);

    List<Animal> findByMeses(Integer meses);

    List<Animal> findByResponsable_IdUsuario(Long responsableIdUsuario);
}