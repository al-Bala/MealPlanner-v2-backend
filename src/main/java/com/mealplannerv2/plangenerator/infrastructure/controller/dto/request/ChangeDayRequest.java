package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.user.model.SavedPrefers;

import java.util.List;

public record ChangeDayRequest(
        SavedPrefers savedPrefers,
        List<MealValues> mealsValues,
        List<String> recipesNamesToChange
) {
}
