package com.mealplannerv2.plangenerator.recipefilter.error;

public class NotFoundMatchingRecipeException extends RuntimeException {
    public NotFoundMatchingRecipeException(String message) {
        super(message);
    }
}
