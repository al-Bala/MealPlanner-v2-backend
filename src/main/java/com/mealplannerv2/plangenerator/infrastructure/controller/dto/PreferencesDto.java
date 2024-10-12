package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.MealValues;
import com.mealplannerv2.user.model.SavedPrefers;
import lombok.Builder;

import java.util.List;

@Builder
public record PreferencesDto(
        SavedPrefers savedPrefers,
        List<MealValues> mealsValues
) {
}
