package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.storage.IngredientDto;

import java.util.List;

class RecipeMapper {

    public static List<RecipeDto> mapFromRecipeListToRecipeDtoList(List<Recipe> recipes){
        return recipes.stream()
                .map(RecipeMapper::mapFromRecipeToRecipeDto)
                .toList();
    }

    public static RecipeDto mapFromRecipeToRecipeDto(Recipe recipe){
        return RecipeDto.builder()
                .id(recipe.id())
                .name(recipe.name())
                .portions(recipe.portions())
                .ingredients(mapFromIngredientToIngredientDto(recipe.ingredients()))
                .build();
    }

    public static List<IngredientDto> mapFromIngredientToIngredientDto(List<Ingredient> ing){
        return ing.stream()
                .map(i -> new IngredientDto(i.name(), (double) i.amount(), i.unit()))
                .toList();
    }

//    public static List<Ingredient> mapFromIngredientDroToIngredient(List<IngredientDto> ing) {
//        return ing.stream()
//                .map(i -> new Ingredient(i.getName(), i.getAmount(), i.getUnit()))
//                .toList();
//    }
}
