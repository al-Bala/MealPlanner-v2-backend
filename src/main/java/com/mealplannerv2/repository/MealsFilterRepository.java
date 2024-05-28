package com.mealplannerv2.repository;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.entity.Recipe;

import java.util.List;

public interface MealsFilterRepository {
    List<Recipe> findMatchingRecipes(InfoForFiltering info);
}
