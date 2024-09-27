package com.mealplannerv2.auth.infrastructure.controller.dto;

import lombok.Builder;

@Builder
public record JwtResponseDto(
        String userId,
        String accessToken,
        String refreshToken
) {
}
