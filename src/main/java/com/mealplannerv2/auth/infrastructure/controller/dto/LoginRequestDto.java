package com.mealplannerv2.auth.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
