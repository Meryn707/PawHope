package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.model.Animal;

import java.util.List;

public interface AnimalService {

    AnimalDTO guardarAnimal (AnimalDTO animalDTO);
    List<AnimalDTO> findAll();
    AnimalDTO findByIdAnimal (Long idAnimal);
}
