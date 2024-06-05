package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.BestMatch;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto2;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.productstorage.ProductStorageFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
class RecipeChooser {

    private final ProductStorageFacade productStorageFacade;

    public RecipeDto getRecipeWithTheMostMatchingOtherIngredients(List<RecipeDto> recipesDto) {
        BestMatch bestMatch = new BestMatch(0, new RecipeDto());

        for(RecipeDto recipe : recipesDto){
            int count = 0;
            for(IngredientDto2 ing : recipe.getIngredients()){
                StoredProductDto productToCheck = new StoredProductDto(ing.getName(), 0.0, ing.getUnit());
                if(productStorageFacade.getStoredProducts().contains(productToCheck)){
                    count++;
                }
            }
            if(bestMatch.matchingScore() < count){
                bestMatch = new BestMatch(count, recipe);
            }
            else if(bestMatch.matchingScore() == count){
                Random random = new Random();
                if(random.nextBoolean()){
                    bestMatch = new BestMatch(count, recipe);
                }
            }
        }
        return bestMatch.recipe();
    }
}
