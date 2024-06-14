package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.UnchangingPrefers;

import java.util.List;

public record FirstDayRequest(
        UnchangingPrefers unchangingPrefers,
        List<ProductFromUser> userProducts,
        String date,
        List<MealValues> mealsValues
) {
}
