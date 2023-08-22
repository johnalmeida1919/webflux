package br.com.johnalmeida1919.webfluxcourse.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ValidationError extends StandardError {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<FieldError> errors = new CopyOnWriteArrayList<>();

    ValidationError(LocalDateTime timestamp, String path, Integer status, String error, String message) {
        super(timestamp, path, status, error, message);
    }

    public void addError(String filedName, String message) {
        errors.add(new FieldError(filedName, message));
    }

    @Getter
    @AllArgsConstructor
    private static final class FieldError {
        private String fieldName;
        private String message;
    }
}
