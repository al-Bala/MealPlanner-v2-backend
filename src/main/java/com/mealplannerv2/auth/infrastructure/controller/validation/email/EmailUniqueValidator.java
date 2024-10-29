package com.mealplannerv2.auth.infrastructure.controller.validation.email;

import com.mealplannerv2.user.UserRepository;
import com.mealplannerv2.user.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    private final UserRepository repository;

    @Override
    public void initialize(EmailUnique param) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        if(email == null){
            return false;
        }
        Optional<User> byEmail = repository.findByEmail(email);
        System.out.println("Unique email: " + byEmail);
        return byEmail.isEmpty();
    }
}