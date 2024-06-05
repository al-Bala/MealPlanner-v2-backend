package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.Builder;

import java.util.List;

@Builder
public record DataForRecipeFiltering(
        int forHowManyDays,
        String diet,
        int timeForPrepareMin,
        List<Ingredient> ingredientsToUseFirstly,
        List<String> productsToAvoid
) {
}
