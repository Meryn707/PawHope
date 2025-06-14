package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.excepciones.UsuarioNoExisteException;
import com.tfg.pawhope.mapper.AnimalMapper;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.AnimalRepository;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final UsuarioRepository usuarioRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository, UsuarioRepository usuarioRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public AnimalDTO animalToDto (Animal animal) {
        return animalMapper.toDto(animal);
    }

    public List<AnimalDTO> findAll() {
        List<Animal> animales = animalRepository.findAll();
        return animalMapper.listToDto(animales);
    }

    public AnimalDTO findByIdAnimal (Long idAnimal) {

        Optional<Animal> animal = animalRepository.findByIdAnimal(idAnimal);

        if (animal.isEmpty()) {
            throw new AnimalNoExisteException("El animal no existe");
        }

        AnimalDTO dto = animalToDto(animal.get());
        dto.setIdAnimal(animal.get().getIdAnimal());
        dto.setIdUsuario(animal.get().getResponsable().getIdUsuario());

        return dto;
    }

    public AnimalDTO guardarAnimal(AnimalDTO animalDTO) {
        Animal animal = animalMapper.toEntity(animalDTO);
        Optional<Usuario> usuario = usuarioRepository.findByIdUsuario(animalDTO.getIdUsuario());

        if (usuario.isEmpty()) {
            throw new UsuarioNoExisteException("El usuario responsable no existe");
        }

        animal.setImagenUrl(animalDTO.getImagenUrl());
        animal.setResponsable(usuario.get());
        animal.setRangoEdad(calcularRangoEdad(animalDTO.getAnios()));

        Animal animalGuardado = animalRepository.save(animal);

        AnimalDTO dto = animalToDto(animalGuardado);
        dto.setIdAnimal(animalGuardado.getIdAnimal());
        dto.setIdUsuario(animalGuardado.getResponsable().getIdUsuario());

        return dto;
    }

    public String calcularRangoEdad (Integer anios) {
        if (anios < 1) return "Cachorro";
        else if (anios <= 5) return "Joven";
        else if (anios <= 10) return "Adulto";
        else return "Senior";
    }

    public List<Animal> filtrarPorEspecie(String especie) {
        if (StringUtils.hasText(especie)) {
            return animalRepository.findByEspecie(especie);
        }
        return animalRepository.findAll();
    }

    public List<Animal> filtrarPorEdad(Integer anios, Integer meses) {
        boolean hayAnios = anios != null;
        boolean hayMeses = meses != null;

        if (hayAnios && hayMeses) {
            return animalRepository.findByAniosAndMeses(anios, meses);
        } else if (hayAnios) {
            return animalRepository.findByAnios(anios);
        } else if (hayMeses) {
            return animalRepository.findByMeses(meses);
        } else {
            return animalRepository.findAll();
        }
    }


    public List<Animal> findByResponsable_IdUsuario(Long responsableIdUsuario) {
        return animalRepository.findByResponsable_IdUsuario(responsableIdUsuario);
    }

    public void deleteAnimal (AnimalDTO animalDTO) {

        if (animalDTO == null || animalDTO.getIdAnimal() == null) {
            throw new IllegalArgumentException("ID de animal no puede ser nulo");
        }

        Animal animal = animalRepository.findById(animalDTO.getIdAnimal()).orElse(null);
        if (animal == null) {
            throw new RuntimeException("Animal no encontrado");
        }

        animalRepository.delete(animal);
    }

    public AnimalDTO actualizarAnimal(AnimalDTO animalDTO) {

        if (animalDTO.getIdAnimal() == null) {
            throw new IllegalArgumentException("ID del animal requerido para la actualización");
        }

        Animal animalExistente = animalRepository.findById(animalDTO.getIdAnimal())
                .orElseThrow(() -> new AnimalNoExisteException("Animal no encontrado"));

        // Solo actualizamos los campos editables
        animalExistente.setNombre(animalDTO.getNombre());
        animalExistente.setEspecie(animalDTO.getEspecie());
        animalExistente.setRaza(animalDTO.getRaza());
        animalExistente.setAnios(animalDTO.getAnios());
        animalExistente.setMeses(animalDTO.getMeses());
        animalExistente.setDescripcion(animalDTO.getDescripcion());

        if (animalDTO.getImagenUrl() != null) {
            animalExistente.setImagenUrl(animalDTO.getImagenUrl()); // si no se sube nueva imagen, se queda la actual
        }

        animalExistente.setRangoEdad(calcularRangoEdad(animalDTO.getAnios()));

        Animal actualizado = animalRepository.save(animalExistente);

        AnimalDTO dto = animalToDto(actualizado);
        dto.setIdUsuario(actualizado.getResponsable().getIdUsuario());
        dto.setIdAnimal(actualizado.getIdAnimal());

        return dto;
    }

}
