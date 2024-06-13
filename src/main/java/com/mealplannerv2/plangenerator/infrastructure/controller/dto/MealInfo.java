package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import java.util.Optional;

public record MealInfo(
        String mealName,
        Integer timeMin,
        Boolean for2Days
) {
}
