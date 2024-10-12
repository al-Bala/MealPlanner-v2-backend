package com.mealplannerv2.plangenerator.infrastructure.controller.dto.response;

import lombok.Builder;

@Builder
public record RecipeResult(
        String typeOfMeal,
        String recipeId,
        String recipeName
//        int forHowManyDays
) {
}
