package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;

public record RecipeForDay(
        String typeOfMeal,
        RecipeDto recipe
) {
}
