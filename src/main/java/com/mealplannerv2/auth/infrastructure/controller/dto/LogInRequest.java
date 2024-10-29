package com.mealplannerv2.auth.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInRequest(

        @NotBlank (message = "{email.require}")
        String email,

        @NotBlank (message = "{password.require}")
        String password
) {
}
