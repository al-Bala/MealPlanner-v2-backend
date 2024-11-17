package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.storage.IngredientDto;

import java.util.List;

public record FirstDayRequest(
        SavedPrefers savedPrefers,
        List<IngredientDto> userProducts,
        List<PanelValuesForMeal> mealsValues
) {
}
