package com.mealplannerv2.auth.infrastructure.controller.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password param) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext ctx) {
        if(password == null || password.isEmpty()){
            return true;
        }
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    }
}