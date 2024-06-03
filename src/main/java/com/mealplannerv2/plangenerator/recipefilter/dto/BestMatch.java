package com.mealplannerv2.plangenerator.recipefilter.dto;

public record BestMatch(
        int matchingScore,
        RecipeDto recipe
) {
}
