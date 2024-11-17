package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.user.model.SavedPrefers;

import java.util.List;

public record NextDayRequest(
        SavedPrefers savedPrefers,
        List<PanelValuesForMeal> mealsValues,
        List<String> usedRecipesNames
) {
}
