package br.com.johnalmeida1919.webfluxcourse.controller.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
    private String verifyDubKey(String message) {
        if(message.contains("email dup key")) {
            return "E-mail already registered";
        }

        return "Dup key exception";
    }
}
