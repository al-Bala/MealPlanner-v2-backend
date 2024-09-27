package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;

public class UserMapper {

    public static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .preferences(user.getPreferences())
                .plans(user.getPlans())
                .build();
    }

    public static User mapFromUserDtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .preferences(userDto.getPreferences())
                .plans(userDto.getPlans())
                .build();
    }
}
