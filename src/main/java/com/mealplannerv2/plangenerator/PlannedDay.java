package com.mealplannerv2.plangenerator;

import lombok.Builder;

import java.util.List;

@Builder
public record PlannedDay(
        String date,
        List<RecipeForDay> recipesForDay
) {
}
