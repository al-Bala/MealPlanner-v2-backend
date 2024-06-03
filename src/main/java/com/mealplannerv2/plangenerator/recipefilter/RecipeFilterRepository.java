package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.InfoFiltering2;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;

import java.util.List;

public interface RecipeFilterRepository {
    List<Recipe> findRecipesWithMatchingIngNamesAndAmounts(InfoFiltering2 info);
    List<Recipe> findRecipesWithMatchingIngNames(InfoFiltering2 info);
}
