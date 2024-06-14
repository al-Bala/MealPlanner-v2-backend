package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.Builder;

import java.util.List;

@Builder
public record DataForRecipeFiltering(
        String typeOfMeal,
        String diet,
        int forHowManyDays,
        Integer timeForPrepareMin,
        List<Ingredient> ingredientsToUseFirstly,
        List<String> productsToAvoid
) {
}
