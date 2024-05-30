package com.mealplannerv2.recipe;

import com.mealplannerv2.repository.IngredientDto;
import com.mealplannerv2.repository.RecipeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static com.mealplannerv2.product.ProductKeeperFacade.productsInUse;

@Component
class RecipeChooser {

    public RecipeDto getTheBestRecipe(List<RecipeDto> recipesDto) {
        BestMatch bestMatch = new BestMatch(0, new RecipeDto());

        for(RecipeDto recipe : recipesDto){
            int count = 0;
            for(IngredientDto ing : recipe.getIngredients()){
                if(productsInUse.contains(ing)){
                    count++;
                }
            }
            if(bestMatch.matchingScore() < count){
                bestMatch = new BestMatch(count, recipe);
            }
            else {
                Random random = new Random();
                if(random.nextBoolean()){
                    bestMatch = new BestMatch(count, recipe);
                }
            }
        }
        return bestMatch.recipe();
    }
}
