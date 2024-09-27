package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.recipe.DayPlan;

import java.util.List;

public record NextDayRequest(
        SavedPrefers savedPrefers,
        List<MealValues> mealsValues,
        List<DayPlan> tempDays
) {
}
