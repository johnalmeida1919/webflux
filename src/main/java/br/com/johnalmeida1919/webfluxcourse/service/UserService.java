package br.com.johnalmeida1919.webfluxcourse.service;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.mapper.UserMapper;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.repository.UserRepository;
import br.com.johnalmeida1919.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return userRepository.save(mapper.toEntity(request));
    }

    public Mono<User> findById (final String id) {
        return handledNotFound(userRepository.findById(id), id);
    }

    private <T> Mono<T> handledNotFound(Mono<T> mono, String id) {
        return  mono.switchIfEmpty(
                Mono.error(new ObjectNotFoundException(format("object not found, Id: %s, type: %s", id, User.class.getSimpleName())))
        );
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request) {
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(userRepository::save);
    }

    public Mono<User> delete(final String id) {
        return handledNotFound(userRepository.findAndRemove(id), id);
    }

}
