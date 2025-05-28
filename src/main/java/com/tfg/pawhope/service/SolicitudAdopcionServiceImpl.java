package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.model.SolicitudAdopcion;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.AnimalRepository;
import com.tfg.pawhope.repository.SolicitudAdopcionRepository;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
public class SolicitudAdopcionServiceImpl {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudAdopcionServiceImpl(SolicitudAdopcionRepository solicitudAdopcionRepository,
                                    AnimalRepository animalRepository,
                                    UsuarioRepository usuarioRepository) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void crearSolicitud(SolicitudAdopcionDTO dto, String correoUsuario) {
        System.out.println("Crear solicitud para animalId: " + dto.getIdAnimal());

        Animal animal = animalRepository.findById(dto.getIdAnimal())
                .orElseThrow(() -> new RuntimeException("Animal no encontrado"));

        Usuario usuario = null;
        if (correoUsuario != null) {
            usuario = usuarioRepository.findByCorreo(correoUsuario).orElse(null);
        }

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setAnimal(animal);
        solicitud.setUsuario(usuario);
        solicitud.setNombre(dto.getNombre());
        solicitud.setEmail(dto.getEmail());
        solicitud.setTelefono(dto.getTelefono());
        solicitud.setMotivo(dto.getMotivo());
        solicitud.setEstado("Pendiente");

        solicitudAdopcionRepository.save(solicitud);
    }

}
