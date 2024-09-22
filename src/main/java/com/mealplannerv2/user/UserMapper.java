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
                .history(user.getHistory())
                .build();
    }
}
