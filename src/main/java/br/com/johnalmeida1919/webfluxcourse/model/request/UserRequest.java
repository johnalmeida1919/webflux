package br.com.johnalmeida1919.webfluxcourse.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
