package com.mealplannerv2.plangenerator;

import com.mealplannerv2.domain.recipefilter.dto.RecipeDto;

public record PlannedDay(
        String date,
        RecipeDto recipe
) {
}
