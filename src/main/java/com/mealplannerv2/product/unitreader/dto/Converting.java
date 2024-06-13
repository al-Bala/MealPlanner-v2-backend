package com.mealplannerv2.product.unitreader.dto;

public record Converting(
        String output_unit,
        double multiplier
) {
}
