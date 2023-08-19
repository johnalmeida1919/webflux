package br.com.johnalmeida1919.webfluxcourse.mapper;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);
}
