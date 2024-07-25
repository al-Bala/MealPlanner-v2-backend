package com.mealplannerv2.recipe;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public record RecipeForDayDb(
        String type_of_meal,
        String recipeId
) {
}
