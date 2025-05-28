package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.AnimalDTO;
import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.AnimalNoExisteException;

import com.tfg.pawhope.model.Usuario;
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

    @GetMapping("/registrar")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("animal", new AnimalDTO());
        return "formulario-registrarAnimal";
    }

   /* @PostMapping
    public String crearAnimal(@ModelAttribute AnimalDTO animalDTO, RedirectAttributes ra) {
        animalServiceImpl.guardarAnimal(animalDTO);
        ra.addFlashAttribute("exito", "Animal registrado correctamente");
        return "redirect:/web/animales";
    }*/

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



}
