package com.mealplannerv2.auth.infrastructure.controller.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String username,
        String accessToken
) {
}
