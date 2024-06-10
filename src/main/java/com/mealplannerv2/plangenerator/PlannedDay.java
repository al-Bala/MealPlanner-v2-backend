package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;

public record PlannedDay(
        String date,
        String typeOfMeal,
        RecipeDto recipe
) {
}
