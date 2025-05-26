package com.tfg.pawhope.service;

import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.repository.AnimalRepository;

import java.util.List;

public class AnimalServiceImpl {

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {this.animalRepository = animalRepository;}

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }
}
