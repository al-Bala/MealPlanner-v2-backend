package com.mealplannerv2;

import com.mealplannerv2.repository.IngredientDto;

import java.util.List;

public record RecipeDto(
        String name,
        String diet,
        List<IngredientDto> ingredients
) {
}
