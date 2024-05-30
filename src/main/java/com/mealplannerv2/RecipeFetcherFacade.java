package com.mealplannerv2;

import com.mealplannerv2.entity.Ingredient;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.IngredientDto;
import com.mealplannerv2.repository.MealsFilterRepositoryImpl;
import com.mealplannerv2.repository.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Log4j2
@Component
public class RecipeFetcherFacade {

    private final MealsFilterRepositoryImpl mealsFilterRepository;

    public RecipeDto fetchRecipeByPreferences(InfoForFiltering info){
        List<Recipe> matchingIngNamesAndAmounts = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);

        if(matchingIngNamesAndAmounts == null){
            log.info("Not found any recipe with matching ingredient's names and amounts.");
            List<Recipe> matchingIngNames = mealsFilterRepository.findRecipesWithMatchingIngNames(info);
            return null;
        }
        else if(matchingIngNamesAndAmounts.size() == 1){
            return mapFromRecipeToRecipeDto(matchingIngNamesAndAmounts.get(0));
        }
        else {
            // TODO: choosing recipe by other ingredients
            Random random = new Random();
            int randomNr = random.nextInt(matchingIngNamesAndAmounts.size());
            return mapFromRecipeToRecipeDto(matchingIngNamesAndAmounts.get(randomNr));
        }
    }

    private RecipeDto mapFromRecipeToRecipeDto(Recipe recipe){
        return new RecipeDto(recipe.name(), mapFromIngredientToIngredientDto(recipe.ingredients()));
    }

    private List<IngredientDto> mapFromIngredientToIngredientDto(List<Ingredient> ing){
        return ing.stream()
                .map(i -> new IngredientDto(i.name(), (double) i.amount(), i.unit()))
                .toList();
    }

}
