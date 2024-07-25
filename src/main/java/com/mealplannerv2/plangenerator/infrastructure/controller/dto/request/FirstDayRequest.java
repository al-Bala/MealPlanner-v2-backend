package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.UnchangingPrefers;
import com.mealplannerv2.storage.IngredientDto;

import java.util.List;

public record FirstDayRequest(
        UnchangingPrefers unchangingPrefers,
        List<IngredientDto> userProducts,
        String date,
        List<MealValues> mealsValues
) {
}
