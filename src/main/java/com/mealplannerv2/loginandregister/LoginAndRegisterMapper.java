package com.mealplannerv2.loginandregister;

import com.mealplannerv2.loginandregister.dto.UserDto;

class LoginAndRegisterMapper {
    public static User mapFromUserDtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .planHistory(userDto.getPlan())
                .build();
    }

    public static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .preferences(user.getPreferences())
                .plan(user.getPlanHistory())
                .build();
    }
}
