package com.mealplannerv2.repository;

public record Ingredient(
        String name,
        int amount,
        String unit
) {
}
