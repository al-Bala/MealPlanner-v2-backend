package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

public record TempRecipe(
        String typeOfMeal,
        String recipeId,
        String recipeName,
        int forHowManyDays,
        boolean isRepeated
) {
    public TempRecipe() {
        this("TYPE_OF_MEAL", "RECIPE_ID", "RECIPE_NAME", 1, false);
    }
}
