package com.mealplannerv2.recipe;

public record RecipeForDayDb(
        String type_of_meal,
        String recipeId,
        String recipeName
) {
}
