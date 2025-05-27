package com.tfg.pawhope.controller.api;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;
import com.tfg.pawhope.excepciones.UsuarioNoExisteException;
import com.tfg.pawhope.service.AnimalService;
import com.tfg.pawhope.service.AnimalServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animales")
public class AnimalApiController {

    private final AnimalServiceImpl animalServiceImpl;

    public AnimalApiController(AnimalServiceImpl animalServiceImpl) {
        this.animalServiceImpl = animalServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<AnimalDTO>> obtenerTodos() {
        return ResponseEntity.ok(animalServiceImpl.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findByIdAnimal (@PathVariable Long id) {

        try {
            AnimalDTO creado = animalServiceImpl.findByIdAnimal(id);
            return ResponseEntity.ok(creado);

        } catch (AnimalNoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearAnimal(@Valid @RequestBody AnimalDTO animalDTO) {

        try {
            AnimalDTO creado = animalServiceImpl.guardarAnimal(animalDTO);
            return ResponseEntity.ok(creado);

        } catch (UsuarioNoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
