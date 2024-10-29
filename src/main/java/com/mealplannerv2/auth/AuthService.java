package com.mealplannerv2.auth;

import com.mealplannerv2.user.UserRepository;
import com.mealplannerv2.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
class AuthService {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserRepository repository;

    public String saveUser(User user) {
        User savedUser = repository.save(user);
        log.info("User: {} added to database", savedUser.getUsername());
        return savedUser.getUsername();
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
