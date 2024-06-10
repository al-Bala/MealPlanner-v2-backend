package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import org.springframework.stereotype.Service;

@Service
class IngredientsCalculator {

    public void setCalculatedIngredients(RecipeDto recipe, int portionsNrToSet, int forHowManyDays) {
        int portionsNrInRecipe = recipe.getPortions();

        if (portionsNrInRecipe != (portionsNrToSet * forHowManyDays)) {
            recipe.setPortions(portionsNrToSet);
            recipe.getIngredients()
                    .forEach(ing -> ing.setAmount(ing.getAmount() * portionsNrToSet / portionsNrInRecipe * forHowManyDays));
        }
    }
}
