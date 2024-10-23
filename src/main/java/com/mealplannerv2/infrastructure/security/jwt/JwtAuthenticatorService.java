package com.mealplannerv2.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mealplannerv2.auth.infrastructure.controller.dto.JwtResponseDto;
import com.mealplannerv2.auth.infrastructure.controller.dto.LogInRequestDto;
import com.mealplannerv2.user.UserFacade;
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
    private final UserFacade userFacade;

    public JwtResponseDto authenticateAndGenerateToken(LogInRequestDto logInRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInRequest.email(), logInRequest.password()));
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        String username = userFacade.getByEmail(email).getUsername();
        String accessToken = createAccessToken(username);
        String refreshToken = createRefreshToken(username);
        return JwtResponseDto.builder()
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(String username){
        Duration expiration = Duration.ofSeconds(properties.accessExpirationSec());
        return buildToken(username, expiration);
    }

    private String createRefreshToken(String username){
        Duration expiration = Duration.ofSeconds(properties.refreshExpirationSec());
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
