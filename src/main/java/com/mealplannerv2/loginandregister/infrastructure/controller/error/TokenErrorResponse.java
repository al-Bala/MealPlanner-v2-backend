package com.mealplannerv2.loginandregister.infrastructure.controller.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponse(
        String message,
        HttpStatus status
) {
}
