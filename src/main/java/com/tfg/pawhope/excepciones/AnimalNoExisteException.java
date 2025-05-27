package com.tfg.pawhope.excepciones;

public class AnimalNoExisteException extends RuntimeException  {
    public AnimalNoExisteException(String message) {
        super(message);
    }
}
