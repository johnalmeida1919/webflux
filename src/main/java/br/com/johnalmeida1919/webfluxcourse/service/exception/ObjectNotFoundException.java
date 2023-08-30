package br.com.johnalmeida1919.webfluxcourse.service.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
