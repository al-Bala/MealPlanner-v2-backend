package com.mealplannerv2.plangenerator.infrastructure.controller.dto.request;

import java.util.List;

public record AcceptDayRequest(
        int portions,
        List<TempRecipe> tempRecipes
) {
}
