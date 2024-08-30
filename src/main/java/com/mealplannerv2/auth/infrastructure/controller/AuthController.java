package com.mealplannerv2.auth.infrastructure.controller;

import com.mealplannerv2.auth.AuthFacade;
import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.dto.LoginRequestDto;
import com.mealplannerv2.auth.infrastructure.controller.dto.LoginTokens;
import com.mealplannerv2.auth.infrastructure.controller.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse registerResponse = authFacade.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDto tokenRequest,
            HttpServletResponse response
    ) {
        LoginTokens loginAuth = authFacade.login(tokenRequest);
        authFacade.setCookie("accessToken", loginAuth.accessToken().getToken(), response);
        authFacade.setCookie("refreshToken", loginAuth.refreshToken().getToken(), response);
        return ResponseEntity.ok(loginAuth.username());
    }

    @GetMapping("/refresh-token")
    public void refreshToken(
            @CookieValue(value = "refreshToken") String token,
            HttpServletResponse response
    ) {
        String newAccessToken = authFacade.refreshToken(token);
        authFacade.setCookie("accessToken", newAccessToken, response);
    }
}
