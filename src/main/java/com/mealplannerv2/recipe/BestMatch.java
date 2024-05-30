package com.mealplannerv2.recipe;

import com.mealplannerv2.repository.RecipeDto;

public record BestMatch(
        int matchingScore,
        RecipeDto recipe
) {
}
