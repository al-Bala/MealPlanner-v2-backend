package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;

import java.util.List;

public interface RecipeFilterRepository {
    List<Recipe> findRecipesWithMatchingIngNamesAndAmounts(DataForRecipeFiltering info);
    List<Recipe> findRecipesWithMatchingIngNames(DataForRecipeFiltering info);
}
