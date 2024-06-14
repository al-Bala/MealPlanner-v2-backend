package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import java.util.List;

public record UnchangingPrefers(
        String diet,
        int portions,
        List<String> productsToAvoid
) {
}
