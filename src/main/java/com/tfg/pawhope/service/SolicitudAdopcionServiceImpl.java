package com.tfg.pawhope.service;

import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.mapper.SolicitudAdopcionMapper;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.model.SolicitudAdopcion;
import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.AnimalRepository;
import com.tfg.pawhope.repository.SolicitudAdopcionRepository;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class SolicitudAdopcionServiceImpl {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudAdopcionMapper solicitudAdopcionMapper;

    public SolicitudAdopcionServiceImpl(SolicitudAdopcionRepository solicitudAdopcionRepository,
                                    AnimalRepository animalRepository,
                                    UsuarioRepository usuarioRepository, SolicitudAdopcionMapper solicitudAdopcionMapper) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudAdopcionMapper = solicitudAdopcionMapper;
    }

    public void crearSolicitud(SolicitudAdopcionDTO dto, String correoUsuario) {

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
        solicitud.setEstado("PENDIENTE");

        solicitudAdopcionRepository.save(solicitud);
    }

    public void guardarSolicitud (SolicitudAdopcionDTO dto) {

        Optional<SolicitudAdopcion> solicitudAdopcion = solicitudAdopcionRepository.findById(dto.getId());

        if (solicitudAdopcion.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada");
        }

        solicitudAdopcion.get().setEstado(dto.getEstado());
        solicitudAdopcionRepository.save(solicitudAdopcion.get());
    }


    public List<SolicitudAdopcion> findByResponsableId (Long idUsuario) {
        return solicitudAdopcionRepository.findByAnimalResponsableIdUsuario(idUsuario);
    }

    public Optional<SolicitudAdopcionDTO> findById(Long id) {

        Optional <SolicitudAdopcion> solicitudAdopcion = solicitudAdopcionRepository.findById(id);

        if (solicitudAdopcion.isEmpty()) {
            throw new RuntimeException("No se encontro la solicitud de adopción");
        }

        SolicitudAdopcionDTO dto = solicitudAdopcionMapper.toDto(solicitudAdopcion.get());

        return Optional.of(dto);
    }



}
