package br.com.johnalmeida1919.webfluxcourse.controller;

import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.model.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserController {

    @PostMapping
    ResponseEntity<Mono<Void>> save(
            @RequestBody UserRequest request
    );

    @GetMapping(value = "/{id}")
    ResponseEntity<Mono<UserResponse>> find(
            @PathVariable String id
    );

    @GetMapping
    ResponseEntity<Flux<UserResponse>> findAll();

    @PatchMapping(value = "/{id}")
    ResponseEntity<Mono<UserResponse>> update(
            @PathVariable String id,
            @RequestBody UserRequest request
    );

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Mono<Void>> delete(@PathVariable String id);

}