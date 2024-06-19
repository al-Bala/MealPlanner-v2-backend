package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.BestMatch;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.productstorage.storedleftovers.StoredLeftoverDto;
import com.mealplannerv2.productstorage.storedleftovers.StoredLeftoversFacade;
import com.mealplannerv2.productstorage.userproducts.UserProductsFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
class RecipeChooser {

    private final StoredLeftoversFacade storedLeftoversFacade;
    private final UserProductsFacade userProductsFacade;

    // recipesDto.size() > 1;
    public RecipeDto getRecipeWithTheMostMatchingOtherIngredients(List<RecipeDto> recipesDto) {
        BestMatch bestMatch = new BestMatch(0, new RecipeDto());
        for(RecipeDto recipe : recipesDto){
            int countUser = 0;
            int countStored = 0;
            for(IngredientDto ing : recipe.getIngredients()){
                StoredLeftoverDto productToCheck = new StoredLeftoverDto(ing.getName(), 0.0, ing.getUnit());
                if(userProductsFacade.getUserProducts().get(productToCheck.getName()) != null) {
                    countUser += 2;
                }
                if(storedLeftoversFacade.getStoredProducts().get(productToCheck.getName()) != null){
                    countStored++;
                }
            }
            int count = countUser + countStored;
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
