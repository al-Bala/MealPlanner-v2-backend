package com.mealplannerv2.repository;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "recipes-test")
public record RecipeTest(
        String _id,
        String name,
        int portions,
        int prepare_time,
        int max_storage_time,
        String diet,
        List<IngredientDto> ingredients,
        List<String> steps
) {
}
