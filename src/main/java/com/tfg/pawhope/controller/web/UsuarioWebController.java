package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.UsuarioDTO;
import com.tfg.pawhope.excepciones.UsuarioNoExisteException;
import com.tfg.pawhope.excepciones.UsuarioYaExisteException;
import com.tfg.pawhope.model.SolicitudAdopcion;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import com.tfg.pawhope.service.UsuarioServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller// para poder devolver vistas/redirecciones
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    private final UsuarioServiceImpl usuarioServiceImpl;
    public final SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl;

    public UsuarioWebController(UsuarioServiceImpl usuarioServiceImpl, SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl) {
        this.usuarioServiceImpl = usuarioServiceImpl;
        this.solicitudAdopcionServiceImpl = solicitudAdopcionServiceImpl;
    }

    @PostMapping
    public String registrarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes ra) {
        System.out.println("TRY ZORRA");
        try {
            System.out.println("USANDO EL SERVICE ASQUEROSO");
            usuarioServiceImpl.registrarUsuario(usuarioDTO);
            return "redirect:/login";
        } catch (UsuarioYaExisteException e) {
            ra.addFlashAttribute("errorCorreoExistente", e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/notificaciones")
    public String verNotificaciones(Model model, Authentication auth) {

        String correo = auth.getName();

        Optional<UsuarioDTO> optionalUsuario = usuarioServiceImpl.findByCorreo(correo);
        if (optionalUsuario.isEmpty()) {
            throw new UsuarioNoExisteException("El usuario no existe");
        }
        UsuarioDTO usuario = optionalUsuario.get();

        List<SolicitudAdopcion> solicitudes = solicitudAdopcionServiceImpl.findByResponsableId(usuario.getIdUsuario());

        // añadimos la lista de solicitudes al modelo para que la vista pueda mostrarlas
        model.addAttribute("solicitudes", solicitudes);

        return "notificaciones";
    }
}
