package com.mealplannerv2.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mealplannerv2.auth.infrastructure.controller.dto.*;
import com.mealplannerv2.auth.token.Token;
import com.mealplannerv2.auth.token.TokenService;
import com.mealplannerv2.auth.token.TokenType;
import com.mealplannerv2.infrastructure.security.jwt.JwtAuthenticatorService;
import com.mealplannerv2.infrastructure.security.jwt.TokenDecoder;
import com.mealplannerv2.user.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class AuthFacade {

    private final AuthService authService;
    private final JwtAuthenticatorService jwtAuthenticatorService;
    private final TokenService tokenService;
    private final TokenDecoder tokenDecoder;

    public AuthResponse register(RegisterRequest userDto) {
        authService.validateRegistration(userDto);
        User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(authService.encodePassword(userDto.password()))
                .build();
        return authService.saveUser(user);
    }

    public AuthResponse login(LoginRequestDto loginCredentials, HttpServletResponse response) {
        JwtResponseDto jwtResponseDto = jwtAuthenticatorService.authenticateAndGenerateToken(loginCredentials);
        Token accessToken = new Token(
                jwtResponseDto.accessToken(),
                jwtResponseDto.username(),
                TokenType.ACCESS
        );
        Token refreshToken = new Token(
                jwtResponseDto.refreshToken(),
                jwtResponseDto.username(),
                TokenType.REFRESH
        );
        response.setHeader("Set-Cookie",
                String.format("%s=%s; Path=%s; HttpOnly; SameSite=%s",
                        "refreshToken", jwtResponseDto.refreshToken(), "/", "Strict"));

        LoginTokens loginTokens = LoginTokens.builder()
                .username(jwtResponseDto.username())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return tokenService.updateAllTokens(loginTokens);
    }

    public AuthResponse refreshToken(String refreshToken) {
        DecodedJWT decodedJWT = tokenDecoder.getDecodedJWT(refreshToken, "RefreshToken");
        String username = decodedJWT.getSubject();
        String newAccessToken = jwtAuthenticatorService.createAccessToken(username);
        Token dbAccessToken = new Token(
                newAccessToken,
                username,
                TokenType.ACCESS
        );
        return tokenService.updateAccessTokens(dbAccessToken);
    }
}
