package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.BestMatch;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.leftovers.LeftoverDto;
import com.mealplannerv2.storage.leftovers.LeftoversFacade;
import com.mealplannerv2.storage.useringredients.UserIngsFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
class RecipeChooser {

    private final LeftoversFacade leftoversFacade;
    private final UserIngsFacade userIngsFacade;

    // recipesDto.size() > 1;
    public RecipeDto getRecipeWithTheMostMatchingOtherIngredients(List<RecipeDto> recipesDto) {
        BestMatch bestMatch = new BestMatch(0, new RecipeDto());
        for(RecipeDto recipe : recipesDto){
            int countUser = 0;
            int countStored = 0;
            for(IngredientDto ing : recipe.getIngredients()){
                LeftoverDto productToCheck = new LeftoverDto(ing.getName(), 0.0, ing.getUnit());
                if(userIngsFacade.getUserIngs().get(productToCheck.getName()) != null) {
                    countUser += 2;
                }
                if(leftoversFacade.getLeftovers().get(productToCheck.getName()) != null){
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
