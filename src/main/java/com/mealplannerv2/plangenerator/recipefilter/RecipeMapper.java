package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;

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

    public static List<Ingredient> mapFromIngredientDroToIngredient(List<IngredientDto> ing) {
        return ing.stream()
                .map(i -> new Ingredient(i.getName(), i.getAmount(), i.getUnit()))
                .toList();
    }
}
