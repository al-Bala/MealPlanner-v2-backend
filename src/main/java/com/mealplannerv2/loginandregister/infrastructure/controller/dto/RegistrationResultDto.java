package com.mealplannerv2.loginandregister.infrastructure.controller.dto;

public record RegistrationResultDto(
        String id,
        boolean created,
        String username
) {
}
