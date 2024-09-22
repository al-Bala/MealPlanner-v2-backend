package com.mealplannerv2.recipe;

import java.util.List;

public record Plan(
        List<DayPlan> days
) {
}
