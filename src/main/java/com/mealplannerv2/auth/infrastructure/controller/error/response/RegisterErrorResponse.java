package com.mealplannerv2.auth.infrastructure.controller.error.response;

public record RegisterErrorResponse(
        boolean isUsernameValid,
        boolean isEmailValid
) {
}
