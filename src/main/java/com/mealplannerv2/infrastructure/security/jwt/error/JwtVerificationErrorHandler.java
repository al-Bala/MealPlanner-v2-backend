package com.mealplannerv2.infrastructure.security.jwt.error;

import com.mealplannerv2.auth.infrastructure.controller.error.response.LoginErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JwtVerificationErrorHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidJwtException.class)
    @ResponseBody
    public LoginErrorResponse handleJWTVerificationException(InvalidJwtException e) {
        System.out.println("Handler: " + e.getMessage());
        return new LoginErrorResponse(e.getMessage());
    }
}
