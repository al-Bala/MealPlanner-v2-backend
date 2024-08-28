package com.mealplannerv2.infrastructure.security.jwt.error;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message) {
        super(message);
    }
}
