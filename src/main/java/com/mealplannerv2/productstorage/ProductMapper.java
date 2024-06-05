package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto2;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;

import java.util.List;

class ProductMapper {

    public static List<RecipeDto> mapFromRecipeListToRecipeDtoList(List<Recipe> recipes){
        return recipes.stream()
                .map(ProductMapper::mapFromRecipeToRecipeDto)
                .toList();
    }

    public static RecipeDto mapFromRecipeToRecipeDto(Recipe recipe){
        return new RecipeDto(recipe.name(), mapFromIngredientToIngredientDto(recipe.ingredients()));
    }

    public static List<IngredientDto2> mapFromIngredientToIngredientDto(List<Ingredient> ing){
        return ing.stream()
                .map(i -> new IngredientDto2(i.name(), (double) i.amount(), i.unit()))
                .toList();
    }

    public static List<Ingredient> mapFromIngredientDroToIngredient(List<IngredientDto2> ing) {
        return ing.stream()
                .map(i -> new Ingredient(i.getName(), i.getAmount(), i.getUnit()))
                .toList();
    }

}
