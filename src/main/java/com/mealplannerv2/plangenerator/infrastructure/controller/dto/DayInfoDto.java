package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;

import java.util.List;

public record DayInfoDto(
        List<Meal> meals
) {
}
