package com.mealplannerv2.recipe.model;

import org.springframework.data.mongodb.core.mapping.Field;

public record Ingredient(
        @Field("name") String name,
        @Field("amount") double amount,
        @Field("unit") String unit
) {
}
