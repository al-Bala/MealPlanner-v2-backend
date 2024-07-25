package com.mealplannerv2.plangenerator.recipefilter.dto;

import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RecipeDto {
    private ObjectId id;
    private String name;
    private int portions;
    private List<IngredientDto> ingredients;

    public RecipeDto() {
    }

    public RecipeDto(String name, List<IngredientDto> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
