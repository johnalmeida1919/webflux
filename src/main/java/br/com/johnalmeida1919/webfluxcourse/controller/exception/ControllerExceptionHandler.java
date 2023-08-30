package br.com.johnalmeida1919.webfluxcourse.controller.exception;

import br.com.johnalmeida1919.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> handleDuplicatedKeyException(
            DuplicateKeyException exception,
            ServerHttpRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        Mono.just(
                                StandardError.builder()
                                        .timestamp(now())
                                        .status(BAD_REQUEST.value())
                                        .error(BAD_REQUEST.getReasonPhrase())
                                        .message(verifyDubKey(exception.getMessage()))
                                        .path(request.getPath().toString())
                                        .build()
                        )
                );

    }

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Mono<StandardError>> handleObjectNotFound(ObjectNotFoundException exception,      ServerHttpRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(
                Mono.just(
                        StandardError
                                .builder()
                                .timestamp(now())
                                .status(NOT_FOUND.value())
                                .error(NOT_FOUND.getReasonPhrase())
                                .message(exception.getMessage())
                                .path(request.getPath().toString())
                                .build()
                )
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException exception, ServerHttpRequest request) {
        var validationError = new ValidationError(
                now(),
                request.getPath().toString(),
                BAD_REQUEST.value(),
                "Validation error",
        "Error on validation attribute"
        );

        for(FieldError fieldError: exception.getFieldErrors()) {
            validationError
                    .addError(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    );
        }

        return ResponseEntity.badRequest().body(Mono.just(validationError));
    }

    private String verifyDubKey(String message) {
        if(message.contains("email dup key")) {
            return "E-mail already registered";
        }

        return "Dup key exception";
    }
}
