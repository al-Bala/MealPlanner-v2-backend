package com.mealplannerv2.auth.infrastructure.controller.dto;

import com.mealplannerv2.auth.token.Token;
import lombok.Builder;

@Builder
public record LoginTokens(
        String username,
        Token accessToken,
        Token refreshToken
) {
}
