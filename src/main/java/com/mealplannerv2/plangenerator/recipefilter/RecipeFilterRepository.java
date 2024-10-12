package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.RecipeFilters;
import com.mealplannerv2.recipe.model.Recipe;

import java.util.List;

public interface RecipeFilterRepository {
    List<Recipe> findRecipesWithMatchingIngNamesAndAmounts(RecipeFilters info);
    List<Recipe> findRecipesWithMatchingIngNames(RecipeFilters info);
}
