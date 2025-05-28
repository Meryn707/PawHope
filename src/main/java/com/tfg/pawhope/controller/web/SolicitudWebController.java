package com.tfg.pawhope.controller.web;

import com.tfg.pawhope.dto.SolicitudAdopcionDTO;
import com.tfg.pawhope.model.SolicitudAdopcion;
import com.tfg.pawhope.service.SolicitudAdopcionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller// para poder devolver vistas/redirecciones
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    public final SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl;

    public SolicitudWebController(SolicitudAdopcionServiceImpl solicitudAdopcionServiceImpl) {
        this.solicitudAdopcionServiceImpl = solicitudAdopcionServiceImpl;
    }

    @GetMapping("/{id}")
    public String verDetalleNotificacion(@PathVariable Long id, Model model) {

        // Buscar la solicitud por id
        Optional<SolicitudAdopcionDTO> solicitudOpt = solicitudAdopcionServiceImpl.findById(id);

        if (solicitudOpt.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada");
        }
        SolicitudAdopcionDTO solicitud = solicitudOpt.get();

        // Añadir la solicitud al modelo para la vista
        model.addAttribute("solicitud", solicitud);

        return "notificacion-detalle";  // Nueva vista para detalle
    }

    @PostMapping("/estado/{id}")
    public String actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            RedirectAttributes redirectAttributes) {

        SolicitudAdopcionDTO solicitud = solicitudAdopcionServiceImpl.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(nuevoEstado);
        solicitudAdopcionServiceImpl.guardarSolicitud(solicitud);

        redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado correctamente.");
        return "redirect:/web/usuarios/notificaciones";
    }


}
