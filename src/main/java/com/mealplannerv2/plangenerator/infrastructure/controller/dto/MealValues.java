package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

public record MealValues(
        String mealName,
        Integer timeMin,
        Integer forHowManyDays
) {
}
