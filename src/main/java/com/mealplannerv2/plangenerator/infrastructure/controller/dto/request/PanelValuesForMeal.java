package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

public record PanelValuesForMeal(
        String mealId,
        Integer timeMin,
        Integer forHowManyDays
) {
}
