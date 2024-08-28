package com.mealplannerv2.auth.infrastructure.controller.error.exception;

import com.mealplannerv2.auth.infrastructure.controller.error.RegisterErrors;
import lombok.Getter;

@Getter
public class RegisterValidationException extends RuntimeException {
    private final RegisterErrors registerErrors;

    public RegisterValidationException(String message, RegisterErrors registerErrors) {
        super(message);
        this.registerErrors = registerErrors;
    }
}
