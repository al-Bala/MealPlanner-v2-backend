package com.mealplannerv2.user;

import com.mealplannerv2.recipe.DayPlan;

import java.util.List;

public record TempPlan(
        String startDateText,
        List<DayPlan> tempDays
) {
}
