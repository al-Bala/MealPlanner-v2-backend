package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record AcceptDayRequest(
        int portions,
        List<TempRecipe> tempRecipes
) {
}
