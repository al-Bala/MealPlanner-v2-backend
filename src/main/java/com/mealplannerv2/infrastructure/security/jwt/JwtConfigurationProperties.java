package com.mealplannerv2.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.jwt")
public record JwtConfigurationProperties(
        String secret,
        long accessExpirationSec,
        long refreshExpirationSec,
        String issuer
) {
}
