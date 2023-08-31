package br.com.johnalmeida1919.webfluxcourse.controller.impl;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.mapper.UserMapper;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.model.response.UserResponse;
import br.com.johnalmeida1919.webfluxcourse.service.UserService;
import br.com.johnalmeida1919.webfluxcourse.service.exception.ObjectNotFoundException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String NAME = "joao";
    public static final String MAIL = "joaoalmeida@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String ID = "1234";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private MongoClient mongoClient;

    @MockBean
    UserMapper userMapper;



    @Test
    @DisplayName("test endpoint save with success")
    void testSaveWithSuccess() {
        when(userService.save(any(UserRequest.class))).thenReturn(just(User.builder().build()));
        final var userRequest = new UserRequest(NAME, MAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(userRequest))
                .exchange()
                .expectStatus().isCreated();

        verify(userService, times(1)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("test endpoint save with duplicated key")
    void testSaveWithoutSuccessEmailAlreadyExists() {
        when(userService.save(any(UserRequest.class))).thenReturn(Mono.error(new DuplicateKeyException("email dup key")));
        final var userRequest = new UserRequest(NAME, MAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(userRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.message").isEqualTo("E-mail already registered");


        verify(userService, times(1)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("test endpoint save with duplicated key")
    void testSaveWithoutSuccessDuplicatedKeyDefault() {
        when(userService.save(any(UserRequest.class))).thenReturn(Mono.error(new DuplicateKeyException("default")));
        final var userRequest = new UserRequest(NAME, MAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(userRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.message").isEqualTo("Dup key exception");


        verify(userService, times(1)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("test endpoint save with bad request")
    void testSaveWithoutSuccess() {

        final var userRequest = new UserRequest(NAME.concat(" "), MAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(userRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.message").isEqualTo("Error on validation attribute")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end");

        verify(userService, times(0)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("test endpoint find by id with success")
    void findByIdWithSuccess() {

        final var user = User.builder().email(MAIL).password(PASSWORD).name(NAME).build();
        when(userService.findById(anyString())).thenReturn(just(user));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse(ID, NAME, MAIL, PASSWORD));

        webTestClient.get().uri("/users/1234")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(MAIL)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.password").isEqualTo(PASSWORD);

    }

    @Test
    @DisplayName("test endpoint find by id with not found")
    void findByIdWithoutSuccess() {

        final var user = User.builder().email(MAIL).password(PASSWORD).name(NAME).build();
        when(userService.findById(anyString())).thenReturn(Mono.error(new ObjectNotFoundException("not found")));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse(ID, NAME, MAIL, PASSWORD));

        webTestClient.get().uri("/users/1234")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/1234")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("not found")
                .jsonPath("$.error").isEqualTo("Not Found");

    }

    @Test
    @DisplayName("test endpoint find all id with success")
    void findAll() {
        final var user = User.builder().email(MAIL).password(PASSWORD).name(NAME).build();
        when(userService.findAll()).thenReturn(Flux.just(user));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse(ID, NAME, MAIL, PASSWORD));

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0]email").isEqualTo(MAIL)
                .jsonPath("$.[0]name").isEqualTo(NAME)
                .jsonPath("$.[0]password").isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("test endpoint update id with success")
    void update() {

        final var user = User.builder().email(MAIL).password(PASSWORD).name(NAME).build();
        when(userService.update(anyString(), any(UserRequest.class))).thenReturn(just(user));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse(ID, NAME, MAIL, PASSWORD));

        webTestClient.patch().uri("/users/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new UserRequest("", "", "")))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(MAIL)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.password").isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("test endpoint delete id with success")
    void delete() {
        final var user = User.builder().email(MAIL).password(PASSWORD).name(NAME).build();
        when(userService.delete(anyString())).thenReturn(just(user));

        webTestClient.delete().uri("/users/1234")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .jsonPath("$").doesNotExist();
    }
}