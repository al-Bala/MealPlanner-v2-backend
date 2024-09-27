package com.mealplannerv2.recipe;

public record RecipeDay(
        String type_of_meal,
        String recipeId,
        String recipeName,
        int forHowManyDays
) {
    public RecipeDay() {
        this("TYPE_OF_MEAL", "RECIPE_ID", "RECIPE_NAME", 1);
    }
}
