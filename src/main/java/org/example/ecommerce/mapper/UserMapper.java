package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.UserResponse;
import org.example.ecommerce.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToUser(User user);
}
