package com.mealplannerv2.auth.infrastructure.controller.error;

import com.mealplannerv2.auth.infrastructure.controller.error.exception.RegisterValidationException;
import com.mealplannerv2.auth.infrastructure.controller.error.response.RegisterErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RegisterErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterValidationException.class)
    @ResponseBody
    public RegisterErrorResponse handleBadRegisterValidation(RegisterValidationException exception) {
        return new RegisterErrorResponse(
                exception.getRegisterErrors().isUsernameValid(),
                exception.getRegisterErrors().isEmailValid()
        );
    }
}