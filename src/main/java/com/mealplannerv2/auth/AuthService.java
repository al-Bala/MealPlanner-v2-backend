package com.mealplannerv2.auth;

import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.dto.RegisterRequest;
import com.mealplannerv2.auth.infrastructure.controller.error.RegisterErrors;
import com.mealplannerv2.auth.infrastructure.controller.error.exception.RegisterValidationException;
import com.mealplannerv2.user.model.User;
import com.mealplannerv2.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Component
class AuthService {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserRepository repository;

    public AuthResponse saveUser(User user) {
        User savedUser = repository.save(user);
        log.info("User: {} added to database", savedUser.getUsername());
        return new AuthResponse(savedUser.getUsername(), "");
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public void validateRegistration(RegisterRequest userDto) {
        RegisterErrors errors = new RegisterErrors();
        Optional<User> byUsername = repository.findByUsername(userDto.username());
        Optional<User> byEmail = repository.findByEmail(userDto.email());

        if(byUsername.isPresent()){
            errors.setUsernameInvalid();
        }
        if(byEmail.isPresent()){
            errors.setEmailInvalid();
        }
        if(!errors.isUsernameValid() || !errors.isEmailValid()){
            throw new RegisterValidationException("Unsuccessful validation.", errors);
        }
    }
}
