package com.mealplannerv2.recipe;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class RecipeFacade {

    private final RecipeRepository recipeRepository;

    public RecipeDto getById(String id) {
        return recipeRepository.findById(id)
                .map(this::mapFromRecipeToRecipeDto)
                .orElseThrow(() -> new RuntimeException("Recipe with id " + id + " not found"));
    }

    private RecipeDto mapFromRecipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.id())
                .name(recipe.name())
                .portions(recipe.portions())
                .ingredients(mapFromIngsToIngsDto(recipe.ingredients()))
                .build();
    }

    private List<IngredientDto> mapFromIngsToIngsDto(List<Ingredient> ings) {
        return ings.stream()
                .map(ing -> new IngredientDto(ing.name(), ing.amount(), ing.unit()))
                .toList();
    }
}
