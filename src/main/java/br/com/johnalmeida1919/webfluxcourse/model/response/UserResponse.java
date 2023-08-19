package br.com.johnalmeida1919.webfluxcourse.model.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) { }
