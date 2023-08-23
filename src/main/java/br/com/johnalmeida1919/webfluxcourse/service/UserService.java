package br.com.johnalmeida1919.webfluxcourse.service;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.mapper.UserMapper;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import br.com.johnalmeida1919.webfluxcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return userRepository.save(mapper.toEntity(request));
    }

    public Mono<User> findById (final String id) {
        return userRepository.findById(id);
    }

}
