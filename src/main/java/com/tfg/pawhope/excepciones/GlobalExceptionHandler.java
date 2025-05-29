package com.tfg.pawhope.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioYaExisteException.class)//captura la execption (asi no es necesario usar try catch)
    public String handleUsuarioYaExiste(UsuarioYaExisteException ex, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("errorCorreoExistente", ex.getMessage()); //addflash permite pasar datos sin que se vean en la url
        return "redirect:/registro";
    }

    @ExceptionHandler(UsuarioNoExisteException.class)
    public String handleUsuarioNoExiste(UsuarioNoExisteException ex, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("errorUsuarioNoExiste", ex.getMessage());
        return "redirect:/inicio"; // redirect provisional
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationsExceptions (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }


    @ExceptionHandler(AnimalNoExisteException.class)
    public String handleAnimalNoExiste(AnimalNoExisteException ex, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("errorAnimalNoExiste", ex.getMessage());
        return "redirect:/";
    }

}
