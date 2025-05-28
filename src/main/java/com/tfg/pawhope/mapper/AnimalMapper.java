package com.tfg.pawhope.mapper;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mapping(target = "imagen", ignore = true)
    AnimalDTO toDto(Animal animal);

    @Mapping(target = "imagen", ignore = true)
    Animal toEntity(AnimalDTO animalDTO);

    List<AnimalDTO> listToDto(List<Animal> animales);
    List<Animal> listToEntity(List<AnimalDTO> dtos);
}
