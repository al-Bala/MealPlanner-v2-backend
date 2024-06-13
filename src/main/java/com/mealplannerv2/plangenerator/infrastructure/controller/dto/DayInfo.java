package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import java.util.List;

public record DayInfo(
        List<MealInfo> mealsInfo
) {
}
