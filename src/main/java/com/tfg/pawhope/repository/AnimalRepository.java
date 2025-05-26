package com.tfg.pawhope.repository;

import com.tfg.pawhope.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
