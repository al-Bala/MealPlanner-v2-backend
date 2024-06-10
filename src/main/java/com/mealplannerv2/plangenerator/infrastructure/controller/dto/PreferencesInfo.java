package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record PreferencesInfo(
        String diet,
        int portions,
        List<String> productsToAvoid,
        List<ProductFromUser> userProducts,
        LocalDate firstDayOfPlan
) {
}
