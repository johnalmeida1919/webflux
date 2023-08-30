package br.com.johnalmeida1919.webfluxcourse.controller.impl;

import br.com.johnalmeida1919.webfluxcourse.controller.UserController;
import br.com.johnalmeida1919.webfluxcourse.mapper.UserMapper;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.model.response.UserResponse;
import br.com.johnalmeida1919.webfluxcourse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private  final UserMapper mapper;
    @Override
    public ResponseEntity<Mono<Void>> save(@Valid UserRequest request) {
        userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request).then());

    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        return ResponseEntity.ok(
                userService.findById(id).map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll().map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
