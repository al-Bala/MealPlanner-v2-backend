package com.mealplannerv2.loginandregister.infrastructure.controller.dto;

public record RegisterUserDto(
        String username,
        String email,
        String password
) {
}
