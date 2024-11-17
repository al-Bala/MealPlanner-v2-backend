package com.mealplannerv2.plangenerator.infrastructure.controller.dto.response;

import lombok.Builder;

@Builder
public record ResultRecipe(
        String mealTypeName,
        String recipeId,
        String recipeName
//        int forHowManyDays
) {
}
