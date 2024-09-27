package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record Preferences(
        SavedPrefers savedPrefers,
        List<MealValues> mealsValues
) {
}
