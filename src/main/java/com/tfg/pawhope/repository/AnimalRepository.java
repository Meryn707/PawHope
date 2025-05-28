package com.tfg.pawhope.repository;

import com.tfg.pawhope.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByIdAnimal(Long idAnimal);

    List<Animal> findByEspecie(String especie);

    List<Animal> findByEdad(Integer edad);

    List<Animal> findByEspecieAndEdad(String especie, Integer edad);

    List<Animal> findByResponsable_IdUsuario(Long responsableIdUsuario);
}