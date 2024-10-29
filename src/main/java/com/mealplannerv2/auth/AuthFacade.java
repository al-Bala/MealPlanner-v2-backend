package com.mealplannerv2.auth;

import com.mealplannerv2.auth.infrastructure.controller.dto.*;
import com.mealplannerv2.auth.token.Token;
import com.mealplannerv2.auth.token.TokenService;
import com.mealplannerv2.auth.token.TokenType;
import com.mealplannerv2.infrastructure.security.jwt.JwtAuthenticatorService;
import com.mealplannerv2.infrastructure.security.jwt.TokenDecoder;
import com.mealplannerv2.user.UserFacade;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Log4j2
@AllArgsConstructor
@Component
public class AuthFacade {

    private final AuthService authService;
    private final JwtAuthenticatorService jwtAuthenticatorService;
    private final TokenService tokenService;
    private final TokenDecoder tokenDecoder;
    private final UserFacade userFacade;

    public String register(RegisterRequest userDto) {
        User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(authService.encodePassword(userDto.password()))
                .preferences(new SavedPrefers())
                .plans(new ArrayList<>())
                .build();
        return authService.saveUser(user);
    }

    public AuthResponse logIn(LogInRequest logInCredentials) {
        JwtResponseDto jwtResponseDto = jwtAuthenticatorService.authenticateAndGenerateToken(logInCredentials);
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
        LoginTokens loginTokens = LoginTokens.builder()
                .username(jwtResponseDto.username())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenService.updateAllTokens(loginTokens);
        return AuthResponse.builder()
                .username(loginTokens.username())
                .accessToken(loginTokens.accessToken().getToken())
                .build();
    }

    public AuthResponse refreshToken(String username) {
        Token refreshToken = tokenService.getRefreshToken(username);
        String token = "";
        // TODO: test
        if(refreshToken != null){
            token = refreshToken.getToken();
        }
        tokenDecoder.getDecodedJWT(token);
        String newAccessToken = jwtAuthenticatorService.createAccessToken(username);
        Token dbAccessToken = new Token(
                newAccessToken,
                username,
                TokenType.ACCESS
        );
        tokenService.updateAccessTokens(dbAccessToken);
        return AuthResponse.builder()
                .username(username)
                .accessToken(newAccessToken)
                .build();
    }
}
