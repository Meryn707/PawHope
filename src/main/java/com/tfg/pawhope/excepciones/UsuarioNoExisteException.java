package com.tfg.pawhope.excepciones;

public class UsuarioNoExisteException extends RuntimeException {
    public UsuarioNoExisteException(String message) {
        super(message);
    }
}
