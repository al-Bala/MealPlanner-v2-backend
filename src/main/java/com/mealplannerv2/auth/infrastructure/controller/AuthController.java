package com.mealplannerv2.auth.infrastructure.controller;

import com.mealplannerv2.auth.AuthFacade;
import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.dto.LoginRequestDto;
import com.mealplannerv2.auth.infrastructure.controller.dto.RegisterRequest;
import com.mealplannerv2.user.UserFacade;
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
    private final UserFacade userFacade;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse registerResponse = authFacade.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequestDto tokenRequest,
            HttpServletResponse response
    ) {
        AuthResponse loginResponse = authFacade.login(tokenRequest, response);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(value = "refreshToken") String token) {
        AuthResponse tokenResponse = authFacade.refreshToken(token);
        return ResponseEntity.ok(tokenResponse);
    }
}
