package com.mealplannerv2.recipe;

import com.mealplannerv2.plangenerator.RecipeForDay;
import lombok.Builder;

import java.util.List;

@Builder
public record PlannedDayDb(
        String day,
        List<RecipeForDayDb> planned_day
) {
}
