package com.mealplannerv2.plangenerator.infrastructure.controller.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DayResult(
        List<RecipeResult> recipesResult
) {
}