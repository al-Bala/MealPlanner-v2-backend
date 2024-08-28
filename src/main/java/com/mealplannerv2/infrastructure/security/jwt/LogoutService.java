package com.mealplannerv2.infrastructure.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mealplannerv2.auth.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
class LogoutService implements LogoutHandler {

    private final TokenDecoder tokenDecoder;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        DecodedJWT jwt = tokenDecoder.getDecodedJWT(token, "Logout");
        tokenService.revokeAllUserTokens(jwt.getSubject());

        response.setHeader("Set-Cookie",
                String.format("%s=%s; Path=%s; HttpOnly; Max-Age=%d; SameSite=%s",
                        "refreshToken", "", "/", 0, "Strict"));
    }
}
