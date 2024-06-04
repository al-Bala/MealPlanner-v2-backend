package com.mealplannerv2.loginandregister.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String username,
        String email,
        String password
) {
}
