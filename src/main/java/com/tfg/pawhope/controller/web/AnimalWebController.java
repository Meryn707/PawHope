package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;

import com.tfg.pawhope.excepciones.UsuarioNoExisteException;
import com.tfg.pawhope.model.Animal;
import com.tfg.pawhope.service.AnimalServiceImpl;
import com.tfg.pawhope.service.UsuarioServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/web/animales")
public class AnimalWebController {

    private final AnimalServiceImpl animalServiceImpl;
    private final UsuarioServiceImpl usuarioServiceImpl;

    public AnimalWebController(AnimalServiceImpl animalServiceImpl,UsuarioServiceImpl usuarioServiceImpl) {
        this.animalServiceImpl = animalServiceImpl;
        this.usuarioServiceImpl = usuarioServiceImpl;
    }



    @GetMapping("/{idAnimal}")
    public String detalleAnimal(
            @PathVariable("idAnimal") Long idAnimal,
            Model model,
            RedirectAttributes redirectAttrs) {

        try {
            AnimalDTO animal = animalServiceImpl.findByIdAnimal(idAnimal);
            model.addAttribute("animal", animal);
            return "animal";
        } catch (AnimalNoExisteException ex) {
            redirectAttrs.addFlashAttribute("error", "Animal no encontrado");
            return "redirect:/";
        }
    }

    @GetMapping("/miAnimal/{idAnimal}")
    public String detalleMiAnimal(
            @PathVariable("idAnimal") Long idAnimal,
            Model model,
            RedirectAttributes redirectAttrs) {

        try {
            AnimalDTO animal = animalServiceImpl.findByIdAnimal(idAnimal);
            model.addAttribute("animal", animal);
            return "mi_animal";
        } catch (AnimalNoExisteException ex) {
            redirectAttrs.addFlashAttribute("error", "Animal no encontrado");
            return "redirect:/";
        }
    }

    @GetMapping("/registrar")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("animal", new AnimalDTO());
        return "formulario-registrarAnimal";
    }


    @PostMapping("/registrar")
    public String crearAnimal(@ModelAttribute AnimalDTO animalDTO,
                              Authentication auth,
                              RedirectAttributes ra) {
        try {
            // Obtén el username o email desde auth
            String username = auth.getName();

            // Consulta el usuario para obtener el idUsuario
            Optional <UsuarioDTO> usuario = usuarioServiceImpl.findByCorreo(username);
            if (usuario.isEmpty()) {
                ra.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/web/animales/registrar";
            }

            // Asigna el idUsuario al DTO antes de guardar
            animalDTO.setIdUsuario(usuario.get().getIdUsuario());

            MultipartFile imagen = animalDTO.getImagen();
            if (imagen != null && !imagen.isEmpty()) {
                String nombreArchivo = guardarArchivo(imagen);
                animalDTO.setImagenUrl(nombreArchivo);
                animalDTO.setImagen(null);
            } else {
                ra.addFlashAttribute("error", "La imagen es obligatoria.");
                return "redirect:/web/animales/registrar";
            }

            animalServiceImpl.guardarAnimal(animalDTO);
            ra.addFlashAttribute("exito", "Animal registrado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al registrar el animal: " + e.getMessage());
            return "redirect:/web/animales/registrar";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String listarAnimales(
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String edad,
            Model model) {

        List<AnimalDTO> animales = animalServiceImpl.findAll();

        if (especie != null) {
            animales.removeIf(a -> !a.getEspecie().equalsIgnoreCase(especie));
        }

        if (edad != null) {
            switch (edad) {
                case "cachorro": // 0 años
                    animales.removeIf(a -> a.getAnios() != 0);
                    break;
                case "joven": // 1-4 años
                    animales.removeIf(a -> a.getAnios() < 1 || a.getAnios() > 4);
                    break;
                case "adulto": // 5-9 años
                    animales.removeIf(a -> a.getAnios() < 5 || a.getAnios() > 9);
                    break;
                case "senior": // 10+ años
                    animales.removeIf(a -> a.getAnios() < 10);
                    break;
            }
        }
        model.addAttribute("animales", animales);
        return "inicio";
    }

    //ugardamos el archivo en uploads
    private String guardarArchivo(MultipartFile archivo) throws IOException {
        String nombreArchivo = System.currentTimeMillis() + "-" + archivo.getOriginalFilename();

        Path rutaCarpeta = Paths.get("uploads");
        if (!Files.exists(rutaCarpeta)) {
            Files.createDirectories(rutaCarpeta);
        }

        Path rutaArchivo = rutaCarpeta.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        // Devuelve la ruta relativa para usarla luego como URL
        return "/uploads/" + nombreArchivo;
    }

    @GetMapping("/mis-animales")
    public String verMisAnimales(Model model, Authentication authentication) {
        String correo = authentication.getName(); // correo es el username
        Optional<UsuarioDTO> optionalUsuario = usuarioServiceImpl.findByCorreo(correo);

        if (optionalUsuario.isEmpty()) {
            throw new UsuarioNoExisteException("Usuario no encontrado");
        }
        System.out.println("ID Usuario autenticado: " + optionalUsuario.get().getIdUsuario()); //
        List<Animal> misAnimales = animalServiceImpl.findByResponsable_IdUsuario(optionalUsuario.get().getIdUsuario());
        model.addAttribute("animales", misAnimales);

        return "mis_animales"; // crea esta vista
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAnimal(@PathVariable Long id, Authentication auth) {

        AnimalDTO animal = animalServiceImpl.findByIdAnimal(id);
        if (animal == null) {
            throw new RuntimeException("Animal no encontrado");
        }

        String correo = auth.getName();
        Optional <UsuarioDTO> usuario = usuarioServiceImpl.findByCorreo(correo);

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!animal.getIdUsuario().equals(usuario.get().getIdUsuario())) {
            throw new RuntimeException("No autorizado");
        }

        animalServiceImpl.deleteAnimal(animal);

        return "redirect:/web/animales/mis-animales";
    }



}
