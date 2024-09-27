package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.recipe.RecipeDay;

import java.util.List;

public record ChangeDayRequest(
        SavedPrefers savedPrefers,
        List<MealValues> mealsValues,
        List<RecipeDay> tempDay
) {
}
