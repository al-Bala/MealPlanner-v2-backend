package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import com.mealplannerv2.recipe.RecipeDay;

import java.util.List;

public record AcceptDayRequest(
        int portions,
        List<RecipeDay> tempDay
) {
}
