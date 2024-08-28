package com.mealplannerv2.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mealplannerv2.auth.infrastructure.controller.dto.JwtResponseDto;
import com.mealplannerv2.auth.infrastructure.controller.dto.LoginRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.*;

@AllArgsConstructor
@Component
public class JwtAuthenticatorService {

    private final JwtConfigurationProperties properties;
    private final AuthenticationManager authenticationManager;
    private final Clock clock;

    public JwtResponseDto authenticateAndGenerateToken(LoginRequestDto tokenRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password()));
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String accessToken = createAccessToken(username);
        String refreshToken = createRefreshToken(username);
        return JwtResponseDto.builder()
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(String username){
//        Duration expiration = Duration.ofMinutes(properties.accessExpirationMin());
        Duration expiration = Duration.ofSeconds(properties.accessExpirationMin());
        return buildToken(username, expiration);
    }

    private String createRefreshToken(String username){
//        Duration expiration = Duration.ofDays(properties.refreshExpirationDays());
        Duration expiration = Duration.ofMinutes(properties.refreshExpirationDays());
        return buildToken(username, expiration);
    }

    private String buildToken(String username, Duration expiration){
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = now.plus(expiration);
        String issuer = properties.issuer();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
