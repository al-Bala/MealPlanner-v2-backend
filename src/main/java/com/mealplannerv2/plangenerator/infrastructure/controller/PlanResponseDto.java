package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;

public record PlanResponseDto(
        RecipeDto recipe
) {
}
