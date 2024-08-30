package com.mealplannerv2.infrastructure.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mealplannerv2.auth.token.TokenService;
import com.mealplannerv2.infrastructure.security.jwt.error.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Log4j2
@Component
@AllArgsConstructor
class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final TokenDecoder tokenDecoder;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
            if(token == null){
                filterChain.doFilter(request, response);
                return;
            }
        }
        final DecodedJWT decodedJWT;
        try {
            decodedJWT = tokenDecoder.getDecodedJWT(token, "Filter");
        } catch (InvalidJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        if(tokenService.isTokenValid(decodedJWT.getToken())){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            decodedJWT.getSubject(),
                            null,
                            Collections.emptyList()
                    );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }


}
