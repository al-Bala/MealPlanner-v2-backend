package com.mealplannerv2.plangenerator.recipefilter.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {
    private String name;
    private List<IngredientDto2> ingredients;

    public RecipeDto(String name, List<IngredientDto2> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public RecipeDto() {
    }
}
