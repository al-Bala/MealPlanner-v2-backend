package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.storage.IngredientDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class IngredientsCalculator {

    public List<IngredientDto> calculateIngredients(RecipeDto recipe, int portionsToSet, int forHowManyDays) {
        int portionsInRecipe = recipe.getPortions();

        if (portionsInRecipe != (portionsToSet * forHowManyDays)) {
            return recipe.getIngredients().stream()
                    .map(ing ->
                        new IngredientDto(
                                ing.getName(),
                                ing.getAmount() * portionsToSet / portionsInRecipe * forHowManyDays,
                                ing.getUnit()
                        ))
                    .toList();
        }
        return recipe.getIngredients();
    }
}
