package com.mealplannerv2;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record InfoForFiltering(
        int forHowManyDays,
        String diet,
        int timeForPrepareMin,
        List<IngredientDto> userProducts,
        List<String> productsToAvoid
) {
}
