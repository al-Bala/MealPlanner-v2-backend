package com.mealplannerv2.loginandregister.infrastructure.controller;

import com.mealplannerv2.loginandregister.LoginAndRegisterFacade;
import com.mealplannerv2.loginandregister.infrastructure.controller.dto.RegistrationResultDto;
import com.mealplannerv2.loginandregister.infrastructure.controller.dto.RegisterUserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@RequestBody RegisterUserDto registerUser) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUser.password());
        RegistrationResultDto registerResult = loginAndRegisterFacade.register(
                new RegisterUserDto(registerUser.username(), registerUser.email(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResult);
    }
}
