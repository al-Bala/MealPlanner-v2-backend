package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

public record MealValues(
        String mealId,
        Integer timeMin,
        Integer forHowManyDays
) {
}
