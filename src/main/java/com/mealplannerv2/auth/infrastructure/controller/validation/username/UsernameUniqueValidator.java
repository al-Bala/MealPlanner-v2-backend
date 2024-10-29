package com.mealplannerv2.auth.infrastructure.controller.validation.username;

import com.mealplannerv2.user.UserRepository;
import com.mealplannerv2.user.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

    private final UserRepository repository;

    @Override
    public void initialize(UsernameUnique param) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext ctx) {
        if(username == null){
            return false;
        }
        Optional<User> byUsername = repository.findByUsername(username);
        System.out.println("User " + byUsername);
        return byUsername.isEmpty();
    }
}