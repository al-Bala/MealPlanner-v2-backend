package com.mealplannerv2.loginandregister;

import com.mealplannerv2.loginandregister.dto.UserDto;

class LoginAndRegisterMapper {
    public static User mapFromUserDtoToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .build();
    }

    public static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .preferences(user.getPreferences())
                .build();
    }
}
