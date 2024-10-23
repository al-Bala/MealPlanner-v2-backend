package com.mealplannerv2.auth.infrastructure.controller;

import com.mealplannerv2.auth.AuthFacade;
import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.dto.LogInRequestDto;
import com.mealplannerv2.auth.infrastructure.controller.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String registeredUser = authFacade.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@Valid @RequestBody LogInRequestDto logInRequest) {
        AuthResponse authResponse = authFacade.logIn(logInRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody String username) {
        AuthResponse newAccessToken = authFacade.refreshToken(username);
        return ResponseEntity.ok(newAccessToken);
    }
}
