package br.com.johnalmeida1919.webfluxcourse.service;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.mapper.UserMapper;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.repository.UserRepository;
import br.com.johnalmeida1919.webfluxcourse.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void save() {
        UserRequest request = new UserRequest("joao", "joao@gmail.com", "1234");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void findById() {

        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.findById("");

        StepVerifier.create(result).expectNextMatches(Objects::nonNull )
                .expectComplete()
                .verify();

        verify(repository, times(1)).findById(anyString());

    }

    @Test
    void update() {
        when(mapper.toEntity( any(UserRequest.class), any(User.class))).thenReturn(User.builder().build());
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));


        Mono<User> result = service.update("", new UserRequest("", "", ""));

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));
        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).findAll();
    }


    @Test
    void delete() {
        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(User.builder().build()));
        Mono<User> result = service.delete("");

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).findAndRemove("");
    }

    @Test
    void handleNotFound() {
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        try {
            service.findById("");
        }catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("object not found, Id:  , type: User.class", e.getMessage());
        }

    }
}