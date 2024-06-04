package com.mealplannerv2.loginandregister.infrastructure.controller.dto;

import lombok.Builder;

@Builder
public record JwtResponseDto(
        String username,
        String token
) {
}
