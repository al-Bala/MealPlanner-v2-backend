package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record Preferences(
        UnchangingPrefers unchangingPrefers,
        LocalDate date,
        List<MealValues> mealsValues
) {
}
