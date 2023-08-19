package br.com.johnalmeida1919.webfluxcourse.mapper;

import br.com.johnalmeida1919.webfluxcourse.entity.User;
import br.com.johnalmeida1919.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);
}
