package com.mealplannerv2.user.planhistory.error;

public class DuplicatePlanException extends RuntimeException {
    public DuplicatePlanException(String message) {
        super(message);
    }
}
