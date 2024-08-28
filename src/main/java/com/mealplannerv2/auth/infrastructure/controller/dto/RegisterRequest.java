package com.mealplannerv2.auth.infrastructure.controller.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
