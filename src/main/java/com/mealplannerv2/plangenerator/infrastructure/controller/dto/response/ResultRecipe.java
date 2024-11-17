package com.mealplannerv2.plangenerator.infrastructure.controller.dto.response;

import lombok.Builder;

@Builder
public record ResultRecipe(
        String nameOfMealType,
        String recipeId,
        String recipeName
//        int forHowManyDays
) {
}
