package com.mealplannerv2.plangenerator.recipefilter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RecipeDto {
    private String name;
    private int portions;
    private List<IngredientDto> ingredients;

    public RecipeDto() {
    }
}
