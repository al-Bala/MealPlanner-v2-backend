package com.mealplannerv2.loginandregister.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
