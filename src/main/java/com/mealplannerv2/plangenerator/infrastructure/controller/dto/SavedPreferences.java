package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import java.util.List;

public record SavedPreferences(
        String diet,
        int portions,
        List<String> productsToAvoid
) {
}
