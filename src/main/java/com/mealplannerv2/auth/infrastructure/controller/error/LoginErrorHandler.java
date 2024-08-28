package com.mealplannerv2.auth.infrastructure.controller.error;

import com.mealplannerv2.auth.infrastructure.controller.error.response.LoginErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LoginErrorHandler {
    private static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public LoginErrorResponse handleBadCredentials() {
        return new LoginErrorResponse(BAD_CREDENTIALS);
    }
}