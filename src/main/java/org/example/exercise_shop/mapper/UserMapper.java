package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.RegisterRequest;
import org.example.exercise_shop.dto.request.UpdateUserProfileRequest;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.example.exercise_shop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
    UserProfileResponse toUserProfileResponse(User user);
    void updateUser(@MappingTarget User user, UpdateUserProfileRequest updateUserProfileRequest);
}
