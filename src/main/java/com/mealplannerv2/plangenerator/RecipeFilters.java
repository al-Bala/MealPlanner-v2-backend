package com.mealplannerv2.plangenerator;

import com.mealplannerv2.recipe.model.Ingredient;
import lombok.Builder;

import java.util.List;

@Builder
public record RecipeFilters(
        String typeOfMeal,
        String dietId,
        int forHowManyDays,
        Integer timeForPrepareMin,
        List<Ingredient> ingredientsToUseFirstly,
        List<String> productsToAvoid
) {
}
