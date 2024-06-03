package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.Builder;

import java.util.List;

@Builder
public record InfoFiltering2(
        int forHowManyDays,
        String diet,
        int timeForPrepareMin,
        List<Ingredient> userProducts,
        List<String> productsToAvoid
) {
}
