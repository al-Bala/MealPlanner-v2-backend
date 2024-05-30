package com.mealplannerv2.recipe;

import com.mealplannerv2.entity.Ingredient;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.IngredientDto;
import com.mealplannerv2.repository.RecipeDto;

import java.util.List;

class RecipeMapper {

    public static List<RecipeDto> mapFromRecipeListToRecipeDtoList(List<Recipe> recipes){
        return recipes.stream()
                .map(RecipeMapper::mapFromRecipeToRecipeDto)
                .toList();
    }

    public static RecipeDto mapFromRecipeToRecipeDto(Recipe recipe){
        return new RecipeDto(recipe.name(), mapFromIngredientToIngredientDto(recipe.ingredients()));
    }

    public static List<IngredientDto> mapFromIngredientToIngredientDto(List<Ingredient> ing){
        return ing.stream()
                .map(i -> new IngredientDto(i.name(), (double) i.amount(), i.unit()))
                .toList();
    }
}
