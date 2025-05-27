package com.tfg.pawhope.repository;

import com.tfg.pawhope.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByIdAnimal(Long idAnimal);
}