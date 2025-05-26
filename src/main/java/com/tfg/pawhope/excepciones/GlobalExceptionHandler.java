package com.tfg.pawhope.excepciones;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioYaExisteException.class)//captura la execption (asi no es necesario usar try catch)

    public String handleUsuarioYaExiste(UsuarioYaExisteException ex, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("errorCorreoExistente", ex.getMessage()); //addflash permite pasar datos sin que se vean en la url
        return "redirect:/registro";
    }
}
