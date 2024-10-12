package com.mealplannerv2.user.model;

public record PlannedRecipe(
        String typeOfMeal,
        String recipeId,
        String recipeName,
        int forHowManyDays
) {
}
