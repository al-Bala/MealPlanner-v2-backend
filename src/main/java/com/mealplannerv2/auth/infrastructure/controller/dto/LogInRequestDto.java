package com.mealplannerv2.auth.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInRequestDto(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
