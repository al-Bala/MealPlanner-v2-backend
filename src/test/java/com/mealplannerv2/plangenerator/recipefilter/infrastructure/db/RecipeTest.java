package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "recipes-test")
public record RecipeTest(
        String _id,
        String name,
        List<String> type_of_meal,
        int portions,
        int prepare_time,
        int max_storage_time,
        String diet,
        List<IngredientTest> ingredients,
        List<String> steps
) {
}
